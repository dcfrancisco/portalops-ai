package com.portalops.assistant.service.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.user.agent.UserAgent;
import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.ai.api.AIProviderType;
import com.portalops.ai.api.AIRequest;
import com.portalops.ai.api.AIResponse;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsContextProvider;
import com.portalops.assistant.api.PortalOpsExecutionMetadata;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.PortalOpsAssistantService;
import com.portalops.assistant.service.internal.configuration.PortalOpsAssistantConfiguration;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.llm.spi.AIProvider;

import java.util.List;
import java.util.Locale;
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

        if (_isUserManagementPrompt(prompt)) {
            AgentResponse agentResponse = _userAgent.execute(prompt);
            _portalOpsContextProvider.recordExecution(
                    new PortalOpsExecutionMetadata(
                            _toExecutionPath(agentResponse),
                            agentResponse.getFindings(),
                            agentResponse.getMessage()));

            if (_log.isInfoEnabled()) {
                _log.info("Routed assistant prompt to UserManagementAgent");
            }

            return new PortalOpsAssistantResponse<>(
                    agentResponse.isSuccess() ? AssistantStatus.SUCCESS :
                            AssistantStatus.ERROR,
                    "User Management",
                    agentResponse.getMessage(),
                    List.of(
                            "Execution path: " +
                                    String.join(" -> ", _toExecutionPath(agentResponse))),
                    agentResponse.isSuccess() ? List.of() :
                            List.of(
                                    "Retry with a user count prompt such as 'How many users are in the portal?'"),
                    List.of(), null);
        }

        AIProvider aiProvider = _aiProviders.get(_providerType);

        if (aiProvider == null) {
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

        AIResponse aiResponse = aiProvider.complete(
                new AIRequest(
                        prompt, Map.of(), portalOpsRequestContext,
                        _portalOpsContextProvider.buildRuntimeContext(),
                        _portalOpsContextProvider.getSystemPrompt()));

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
                List.of(
                        "Provider: " + aiResponse.getProviderType(),
                        "Model: " + _getValue(aiResponse.getModelName(), "Default")),
                List.of(), List.of(), null);
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
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

    private boolean _isUserManagementPrompt(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

        return normalizedPrompt.contains("user") &&
                (normalizedPrompt.contains("count") ||
                 normalizedPrompt.contains("how many"));
    }

    private List<String> _toExecutionPath(AgentResponse agentResponse) {
        List<String> executionPath = new java.util.ArrayList<>();

        executionPath.add("PortalOps Assistant");
        executionPath.addAll(agentResponse.getExecutionPath());

        return executionPath;
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsAssistantServiceComponent.class);

    private final Map<String, AIProvider> _aiProviders =
            new ConcurrentHashMap<>();
    private volatile String _providerType = AIProviderType.OPENAI.name();

    @Reference
    private PortalOpsContextProvider _portalOpsContextProvider;

    @Reference
    private UserAgent _userAgent;

}
