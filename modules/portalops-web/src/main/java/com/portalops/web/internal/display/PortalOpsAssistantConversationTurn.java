package com.portalops.web.internal.display;

import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.AssistantPayload;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsAssistantConversationTurn implements Serializable {

    public PortalOpsAssistantConversationTurn(
            String prompt,
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        _portalOpsAssistantResponse = Objects.requireNonNull(
                portalOpsAssistantResponse);
        _prompt = Objects.requireNonNull(prompt);
    }

    public PortalOpsAssistantResponse<? extends AssistantPayload>
            getPortalOpsAssistantResponse() {

        return _portalOpsAssistantResponse;
    }

    public String getPrompt() {
        return _prompt;
    }

    private final PortalOpsAssistantResponse<? extends AssistantPayload>
            _portalOpsAssistantResponse;
    private final String _prompt;

}
