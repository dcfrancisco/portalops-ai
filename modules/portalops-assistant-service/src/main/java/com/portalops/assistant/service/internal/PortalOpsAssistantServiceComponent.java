package com.portalops.assistant.service.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.petra.string.StringBundler;

import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.content.ContentSummary;
import com.portalops.api.site.SiteInspectionService;
import com.portalops.api.site.SiteSummary;
import com.portalops.agent.user.agent.UserAgent;
import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.dto.UserQueryData;
import com.portalops.ai.api.AIProviderType;
import com.portalops.ai.api.AIRequest;
import com.portalops.ai.api.AIResponse;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsContextProvider;
import com.portalops.assistant.api.PortalOpsExecutionMetadata;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.PortalOpsAssistantService;
import com.portalops.assistant.api.payload.AssistantPayload;
import com.portalops.assistant.api.payload.ContentFindingsPayload;
import com.portalops.assistant.api.payload.SiteFindingsPayload;
import com.portalops.assistant.api.payload.UserFindingsPayload;
import com.portalops.assistant.service.internal.configuration.PortalOpsAssistantConfiguration;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.llm.spi.AIProvider;

import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
        configurationPid = "com.portalops.assistant.service.internal.configuration.PortalOpsAssistantConfiguration",
        service = PortalOpsAssistantService.class
)
public class PortalOpsAssistantServiceComponent
        implements PortalOpsAssistantService {

    @Override
    public PortalOpsAssistantResponse<?> chat(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        AIProvider aiProvider = _aiProviders.get(_providerType);

        if (aiProvider == null) {
            return _getProviderUnavailableResponse();
        }

        if (_isUserManagementPrompt(prompt)) {
            AgentResponse agentResponse = _userAgent.execute(prompt);

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to UserManagementAgent");
            }

            if (!agentResponse.isSuccess()) {
                return new PortalOpsAssistantResponse<>(
                        AssistantStatus.ERROR, "User Management",
                        "PortalOps could not collect user data.",
                        List.of("Error code: " + agentResponse.getErrorCode()),
                        List.of(
                                "Verify the UserManagementAgent and related user skills are active."),
                        List.of(), null);
            }

            PortalOpsExecutionMetadata portalOpsExecutionMetadata =
                    new PortalOpsExecutionMetadata(
                            JSONFactoryUtil.looseSerializeDeep(
                                    agentResponse.getData()),
                            _toExecutionPath(agentResponse));

            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    portalOpsExecutionMetadata,
                    _toUserFindingsPayload(
                            (UserQueryData)agentResponse.getData()));
        }

        if (_isSiteManagementPrompt(prompt)) {
            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    _toSiteExecutionMetadata(prompt, portalOpsRequestContext),
                    _toSiteFindingsPayload(prompt, portalOpsRequestContext));
        }

        if (_isContentManagementPrompt(prompt)) {
            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    _toContentExecutionMetadata(prompt, portalOpsRequestContext),
                    _toContentFindingsPayload(prompt, portalOpsRequestContext));
        }

        return _completeWithOpenAI(
                aiProvider, prompt, portalOpsRequestContext, null, null);
    }

    private PortalOpsAssistantResponse<?> _completeWithOpenAI(
            AIProvider aiProvider, String prompt,
            PortalOpsRequestContext portalOpsRequestContext,
            PortalOpsExecutionMetadata portalOpsExecutionMetadata,
            AssistantPayload assistantPayload) {

        AIResponse aiResponse = aiProvider.complete(
                new AIRequest(
                        prompt, Map.of(), portalOpsRequestContext,
                        _portalOpsContextProvider.buildRuntimeContext(
                                portalOpsExecutionMetadata),
                        _getSystemPrompt()));

        if (!aiResponse.isSuccess()) {
            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.ERROR, "Assistant Request Failed",
                    aiResponse.getErrorMessage(),
                    List.of(
                            "Provider: " + aiResponse.getProviderType(),
                            "Model: " + _getValue(aiResponse.getModelName(), "Not configured")),
                    List.of(
                            "Verify the provider API key and model configuration in System Settings."),
                    List.of(), null);
        }

        return new PortalOpsAssistantResponse<>(
                AssistantStatus.SUCCESS,
                aiResponse.getProviderType() + " Assistant",
                aiResponse.getContent(),
                List.of(), List.of(), List.of(), assistantPayload);
    }

    private PortalOpsAssistantResponse<?> _getProviderUnavailableResponse() {
        return new PortalOpsAssistantResponse<>(
                AssistantStatus.ERROR, "Assistant Provider Unavailable",
                "PortalOps could not find a provider for " + _providerType +
                        ".",
                List.of(
                        "No AI provider is registered for the configured provider type."),
                List.of(
                        "Review PortalOps Assistant system settings and provider module deployment."),
                List.of(), null);
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _additionalSystemPrompt = _getString(
                properties, "additionalSystemPrompt");
        _providerType = _getValue(
                _getString(properties, "providerType"),
                AIProviderType.OPENAI.name());
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeAIProvider"
    )
    protected void addAIProvider(AIProvider aiProvider) {
        _aiProviders.put(aiProvider.getProviderType(), aiProvider);
    }

    protected void removeAIProvider(AIProvider aiProvider) {
        _aiProviders.remove(aiProvider.getProviderType());
    }

    private String _getValue(String value, String fallback) {
        if ((value == null) || value.isBlank()) {
            return fallback;
        }

        return value;
    }

    private String _getString(Map<String, Object> properties, String key) {
        Object value = properties.get(key);

        if (value == null) {
            return null;
        }

        return String.valueOf(value).trim();
    }

    private String _getSystemPrompt() {
        String systemPrompt = _portalOpsContextProvider.getSystemPrompt();

        if ((_additionalSystemPrompt == null) ||
            _additionalSystemPrompt.isBlank()) {

            return systemPrompt;
        }

        return StringBundler.concat(
                systemPrompt, "\n\nAdditional administrator instructions:\n",
                _additionalSystemPrompt);
    }

    private boolean _isUserManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("user") ||
                normalizedPrompt.contains("account") ||
                normalizedPrompt.contains("administrator") ||
                normalizedPrompt.contains("administrators") ||
                normalizedPrompt.contains("admin") ||
                normalizedPrompt.contains("lockout") ||
                normalizedPrompt.contains("locked");
    }

    private boolean _isSiteManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("site");
    }

    private boolean _isContentManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("content") ||
                normalizedPrompt.contains("article") ||
                normalizedPrompt.contains("draft") ||
                normalizedPrompt.contains("expired");
    }

    private List<String> _toExecutionPath(AgentResponse agentResponse) {
        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.add(_userAgent.getName());
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private UserFindingsPayload _toUserFindingsPayload(
            UserQueryData userQueryData) {

        return new UserFindingsPayload(
                userQueryData.getActiveUsers(),
                userQueryData.getAdministratorAccounts(),
                userQueryData.getInactiveUsers(),
                userQueryData.getLockedUsers(),
                userQueryData.getMatchedUsers(), userQueryData.getQueryType(),
                userQueryData.getTotalUsers());
    }

    private AssistantPayload _toContentFindingsPayload(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        List<ContentSummary> contentSummaries =
                _getContentSummaries(prompt, portalOpsRequestContext);
        List<ContentSummary> allContentSummaries =
                _contentInspectionService.getContentSummary(
                        portalOpsRequestContext);
        List<ContentSummary> expiredContentSummaries =
                _contentInspectionService.getExpiredContent(
                        portalOpsRequestContext);
        List<ContentSummary> pendingContentSummaries =
                _contentInspectionService.getPendingContent(
                        portalOpsRequestContext);

        return new ContentFindingsPayload(
                expiredContentSummaries.size(), contentSummaries.size(),
                pendingContentSummaries.size(), _getContentQueryType(prompt),
                allContentSummaries.size());
    }

    private PortalOpsExecutionMetadata _toContentExecutionMetadata(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        List<ContentSummary> contentSummaries =
                _getContentSummaries(prompt, portalOpsRequestContext);
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("queryType", _getContentQueryType(prompt));
        data.put("totalContent",
                _contentInspectionService.getContentSummary(
                        portalOpsRequestContext).size());
        data.put("expiredContent",
                _contentInspectionService.getExpiredContent(
                        portalOpsRequestContext).size());
        data.put("pendingContent",
                _contentInspectionService.getPendingContent(
                        portalOpsRequestContext).size());
        data.put("content", contentSummaries);

        return new PortalOpsExecutionMetadata(
                JSONFactoryUtil.looseSerializeDeep(data),
                List.of(
                        "PortalOps Assistant",
                        "PortalOpsContentInspectionService"));
    }

    private String _getContentQueryType(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        if (normalizedPrompt.contains("expired")) {
            return "expired-content";
        }

        if (normalizedPrompt.contains("pending") ||
            normalizedPrompt.contains("draft")) {

            return "pending-content";
        }

        return "content-summary";
    }

    private List<ContentSummary> _getContentSummaries(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        String queryType = _getContentQueryType(prompt);

        if ("expired-content".equals(queryType)) {
            return _contentInspectionService.getExpiredContent(
                    portalOpsRequestContext);
        }

        if ("pending-content".equals(queryType)) {
            return _contentInspectionService.getPendingContent(
                    portalOpsRequestContext);
        }

        return _contentInspectionService.getContentSummary(
                portalOpsRequestContext);
    }

    private AssistantPayload _toSiteFindingsPayload(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        List<SiteSummary> siteSummaries =
                _getSiteSummaries(prompt, portalOpsRequestContext);
        List<SiteSummary> allSiteSummaries = _siteInspectionService.getSites(
                portalOpsRequestContext);

        return new SiteFindingsPayload(
                (int)allSiteSummaries.stream(
                ).filter(
                        SiteSummary::isActive
                ).count(),
                siteSummaries.size(), _getSiteQueryType(prompt),
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getUserCount
                ).sum(),
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getPrivatePages
                ).sum(),
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getPublicPages
                ).sum(),
                allSiteSummaries.size());
    }

    private PortalOpsExecutionMetadata _toSiteExecutionMetadata(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        List<SiteSummary> siteSummaries =
                _getSiteSummaries(prompt, portalOpsRequestContext);
        List<SiteSummary> allSiteSummaries = _siteInspectionService.getSites(
                portalOpsRequestContext);
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("queryType", _getSiteQueryType(prompt));
        data.put("totalSites", allSiteSummaries.size());
        data.put("activeSites",
                allSiteSummaries.stream(
                ).filter(
                        SiteSummary::isActive
                ).count());
        data.put("totalMemberships",
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getUserCount
                ).sum());
        data.put("totalPublicPages",
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getPublicPages
                ).sum());
        data.put("totalPrivatePages",
                allSiteSummaries.stream(
                ).mapToInt(
                        SiteSummary::getPrivatePages
                ).sum());
        data.put("sites", siteSummaries);

        return new PortalOpsExecutionMetadata(
                JSONFactoryUtil.looseSerializeDeep(data),
                List.of(
                        "PortalOps Assistant",
                        "PortalOpsSiteInspectionService"));
    }

    private String _getSiteQueryType(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        if (normalizedPrompt.contains("membership") ||
            normalizedPrompt.contains("member")) {

            return "site-membership";
        }

        if (normalizedPrompt.contains("activity")) {
            return "site-activity";
        }

        return "sites";
    }

    private List<SiteSummary> _getSiteSummaries(
            String prompt, PortalOpsRequestContext portalOpsRequestContext) {

        String queryType = _getSiteQueryType(prompt);

        if ("site-membership".equals(queryType)) {
            return _siteInspectionService.getSiteMembership(
                    portalOpsRequestContext);
        }

        if ("site-activity".equals(queryType)) {
            return _siteInspectionService.getSiteActivity(
                    portalOpsRequestContext);
        }

        return _siteInspectionService.getSites(portalOpsRequestContext);
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsAssistantServiceComponent.class);

    private final Map<String, AIProvider> _aiProviders =
            new ConcurrentHashMap<>();
    private volatile String _additionalSystemPrompt;
    private volatile String _providerType = AIProviderType.OPENAI.name();

    @Reference
    private PortalOpsContextProvider _portalOpsContextProvider;

    @Reference
    private ContentInspectionService _contentInspectionService;

    @Reference
    private SiteInspectionService _siteInspectionService;

    @Reference
    private UserAgent _userAgent;

}
