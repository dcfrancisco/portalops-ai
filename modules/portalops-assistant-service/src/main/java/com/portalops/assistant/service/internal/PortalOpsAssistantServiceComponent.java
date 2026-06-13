package com.portalops.assistant.service.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.petra.string.StringBundler;

import com.portalops.agent.content.agent.ContentAgent;
import com.portalops.agent.content.dto.ContentQueryData;
import com.portalops.agent.management.agent.ManagementAgent;
import com.portalops.agent.management.dto.ManagementQueryData;
import com.portalops.agent.search.agent.SearchAgent;
import com.portalops.agent.search.dto.SearchQueryData;
import com.portalops.agent.site.agent.SiteAgent;
import com.portalops.agent.site.dto.SiteQueryData;
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
import com.portalops.assistant.api.payload.ManagementFindingsPayload;
import com.portalops.assistant.api.payload.SearchFindingsPayload;
import com.portalops.assistant.api.payload.SiteFindingsPayload;
import com.portalops.assistant.api.payload.UserFindingsPayload;
import com.portalops.assistant.service.internal.configuration.PortalOpsAssistantConfiguration;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.llm.spi.AIProvider;

import java.util.List;
import java.util.Map;
import java.util.Locale;
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

        if (_isManagementPrompt(prompt)) {
            com.portalops.agent.management.dto.AgentResponse agentResponse =
                    _managementAgent.execute(prompt);

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to PortalOpsManagementAgent");
            }

            if (!agentResponse.isSuccess()) {
                return new PortalOpsAssistantResponse<>(
                        AssistantStatus.ERROR, "PortalOps Management",
                        "PortalOps could not collect runtime metadata.",
                        List.of("Error code: " + agentResponse.getErrorCode()),
                        List.of(
                                "Verify the PortalOpsManagementAgent and related management skills are active."),
                        List.of(), null);
            }

            PortalOpsExecutionMetadata portalOpsExecutionMetadata =
                    new PortalOpsExecutionMetadata(
                            JSONFactoryUtil.looseSerializeDeep(
                                    agentResponse.getData()),
                            _toManagementExecutionPath(agentResponse));

            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    portalOpsExecutionMetadata,
                    _toManagementFindingsPayload(
                            (ManagementQueryData)agentResponse.getData()));
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
            com.portalops.agent.site.dto.AgentResponse agentResponse =
                    _siteAgent.execute(prompt);

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to SiteManagementAgent");
            }

            if (!agentResponse.isSuccess()) {
                return new PortalOpsAssistantResponse<>(
                        AssistantStatus.ERROR, "Site Management",
                        "PortalOps could not collect site data.",
                        List.of("Error code: " + agentResponse.getErrorCode()),
                        List.of(
                                "Verify the SiteManagementAgent and related site skills are active."),
                        List.of(), null);
            }

            PortalOpsExecutionMetadata portalOpsExecutionMetadata =
                    new PortalOpsExecutionMetadata(
                            JSONFactoryUtil.looseSerializeDeep(
                                    agentResponse.getData()),
                            _toSiteExecutionPath(agentResponse));

            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    portalOpsExecutionMetadata,
                    _toSiteFindingsPayload(
                            (SiteQueryData)agentResponse.getData()));
        }

        if (_isSearchManagementPrompt(prompt)) {
            com.portalops.agent.search.dto.AgentResponse agentResponse =
                    _searchAgent.execute(prompt);

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to SearchManagementAgent");
            }

            if (!agentResponse.isSuccess()) {
                return new PortalOpsAssistantResponse<>(
                        AssistantStatus.ERROR, "Search Management",
                        "PortalOps could not collect search data.",
                        List.of("Error code: " + agentResponse.getErrorCode()),
                        List.of(
                                "Verify the SearchManagementAgent and related search skills are active."),
                        List.of(), null);
            }

            PortalOpsExecutionMetadata portalOpsExecutionMetadata =
                    new PortalOpsExecutionMetadata(
                            JSONFactoryUtil.looseSerializeDeep(
                                    agentResponse.getData()),
                            _toSearchExecutionPath(agentResponse));

            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    portalOpsExecutionMetadata,
                    _toSearchFindingsPayload(
                            (SearchQueryData)agentResponse.getData()));
        }

        if (_isContentManagementPrompt(prompt)) {
            com.portalops.agent.content.dto.AgentResponse agentResponse =
                    _contentAgent.execute(prompt);

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to ContentManagementAgent");
            }

            if (!agentResponse.isSuccess()) {
                return new PortalOpsAssistantResponse<>(
                        AssistantStatus.ERROR, "Content Management",
                        "PortalOps could not collect content data.",
                        List.of("Error code: " + agentResponse.getErrorCode()),
                        List.of(
                                "Verify the ContentManagementAgent and related content skills are active."),
                        List.of(), null);
            }

            PortalOpsExecutionMetadata portalOpsExecutionMetadata =
                    new PortalOpsExecutionMetadata(
                            JSONFactoryUtil.looseSerializeDeep(
                                    agentResponse.getData()),
                            _toContentExecutionPath(agentResponse));

            return _completeWithOpenAI(
                    aiProvider, prompt, portalOpsRequestContext,
                    portalOpsExecutionMetadata,
                    _toContentFindingsPayload(
                            (ContentQueryData)agentResponse.getData()));
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

    private boolean _isManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("what can portalops do") ||
                normalizedPrompt.contains("what can you do") ||
                normalizedPrompt.contains("list capabilities") ||
                normalizedPrompt.contains("list agents") ||
                normalizedPrompt.contains("list skills") ||
                normalizedPrompt.contains("list domains") ||
                normalizedPrompt.startsWith("describe ");
    }

    private boolean _isSiteManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("site") ||
                normalizedPrompt.contains("page") ||
                normalizedPrompt.contains("pages");
    }

    private boolean _isSearchManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("search") ||
                normalizedPrompt.contains("reindex") ||
                normalizedPrompt.contains("indexing");
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

    private List<String> _toManagementExecutionPath(
            com.portalops.agent.management.dto.AgentResponse agentResponse) {

        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.add(_managementAgent.getName());
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private AssistantPayload _toManagementFindingsPayload(
            ManagementQueryData managementQueryData) {

        return new ManagementFindingsPayload(
                managementQueryData.getQueryType(),
                managementQueryData.getSubject(),
                managementQueryData.getTotalAgents(),
                managementQueryData.getTotalCapabilities(),
                managementQueryData.getTotalDomains(),
                managementQueryData.getTotalSkills());
    }

    private List<String> _toContentExecutionPath(
            com.portalops.agent.content.dto.AgentResponse agentResponse) {

        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.add(_contentAgent.getName());
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private AssistantPayload _toContentFindingsPayload(
            ContentQueryData contentQueryData) {

        return new ContentFindingsPayload(
                contentQueryData.getExpiredContent(),
                contentQueryData.getMatchedContent(),
                contentQueryData.getPendingContent(),
                contentQueryData.getQueryType(),
                contentQueryData.getTotalContent());
    }

    private List<String> _toSearchExecutionPath(
            com.portalops.agent.search.dto.AgentResponse agentResponse) {

        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.add(_searchAgent.getName());
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private AssistantPayload _toSearchFindingsPayload(
            SearchQueryData searchQueryData) {

        return new SearchFindingsPayload(
                searchQueryData.getDiagnostics().size(),
                searchQueryData.getHealthState(),
                searchQueryData.getIndexedDocuments(),
                searchQueryData.isIndexExists(),
                searchQueryData.isIndexReadOnly(),
                searchQueryData.getLastReindexDate(),
                searchQueryData.getQueryType(),
                searchQueryData.getReindexTaskCount(),
                searchQueryData.isReindexRequired(),
                searchQueryData.isSearchEnabled(),
                searchQueryData.getSearchEngine(),
                searchQueryData.getWarnings().size());
    }

    private List<String> _toSiteExecutionPath(
            com.portalops.agent.site.dto.AgentResponse agentResponse) {

        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.add(_siteAgent.getName());
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private AssistantPayload _toSiteFindingsPayload(
            SiteQueryData siteQueryData) {

        return new SiteFindingsPayload(
                siteQueryData.getActiveSites(),
                siteQueryData.getMatchedPrivatePages(),
                siteQueryData.getMatchedPublicPages(),
                siteQueryData.getMatchedSites(),
                siteQueryData.getQueryType(),
                siteQueryData.getSitesWithoutPages(),
                siteQueryData.getTotalMemberships(),
                siteQueryData.getTotalPrivatePages(),
                siteQueryData.getTotalPublicPages(),
                siteQueryData.getTotalSites());
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
    private ContentAgent _contentAgent;

    @Reference
    private ManagementAgent _managementAgent;

    @Reference
    private SearchAgent _searchAgent;

    @Reference
    private SiteAgent _siteAgent;

    @Reference
    private UserAgent _userAgent;

}
