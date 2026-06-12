package com.portalops.assistant.api;

import com.portalops.assistant.api.payload.AssistantPayload;

public interface AssistantCommandRouter {

    public PortalOpsAssistantResponse<? extends AssistantPayload> route(
            PortalOpsAssistantRequest portalOpsAssistantRequest);

}
