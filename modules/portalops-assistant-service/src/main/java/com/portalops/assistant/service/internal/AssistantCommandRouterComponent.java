package com.portalops.assistant.service.internal;

import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantCommandRegistry;
import com.portalops.assistant.api.AssistantCommandRouter;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.AssistantPayload;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandRouter.class)
public class AssistantCommandRouterComponent
        implements AssistantCommandRouter {

    @Override
    public PortalOpsAssistantResponse<? extends AssistantPayload> route(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        AssistantCommandHandler assistantCommandHandler =
                _assistantCommandRegistry.getAssistantCommandHandler(
                        portalOpsAssistantRequest.getCommand());

        if (assistantCommandHandler == null) {
            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.ERROR, "Unsupported Assistant Command",
                    "PortalOps Assistant could not locate a handler for the selected command.",
                    List.of(
                            "No deterministic handler is registered for " +
                                    portalOpsAssistantRequest.getCommand() + "."),
                    List.of(
                            "Retry with one of the supported PortalOps Assistant commands."),
                    List.of(new AssistantAction("Show System Health",
                            "SHOW_SYSTEM_HEALTH")),
                    null);
        }

        return assistantCommandHandler.execute(portalOpsAssistantRequest);
    }

    @Reference
    private AssistantCommandRegistry _assistantCommandRegistry;

}
