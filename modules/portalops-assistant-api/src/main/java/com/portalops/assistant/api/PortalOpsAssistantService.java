package com.portalops.assistant.api;

import com.portalops.api.service.PortalOpsRequestContext;

public interface PortalOpsAssistantService {

    public PortalOpsAssistantResponse<?> chat(
            String prompt, PortalOpsRequestContext portalOpsRequestContext);

}
