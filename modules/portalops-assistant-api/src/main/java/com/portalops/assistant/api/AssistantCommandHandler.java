package com.portalops.assistant.api;

import com.portalops.assistant.api.payload.AssistantPayload;

public interface AssistantCommandHandler {

    public PortalOpsAssistantResponse<? extends AssistantPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest);

    public AssistantCommand getCommand();

}
