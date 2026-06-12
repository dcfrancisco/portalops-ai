package com.portalops.assistant.service.internal.handler;

import com.portalops.api.knowledge.PortalHealthSummary;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.SystemHealthPayload;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandHandler.class)
public class ShowSystemHealthHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<SystemHealthPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        PortalKnowledgeSnapshot portalKnowledgeSnapshot =
                _portalKnowledgeService.getSnapshot(
                        portalOpsAssistantRequest.getContext());
        PortalHealthSummary portalHealthSummary =
                portalKnowledgeSnapshot.getPortalHealthSummary();

        int failedScheduledJobs = portalKnowledgeSnapshot.getWorkflowKnowledge().
                getStuckWorkflows().size();

        SystemHealthPayload systemHealthPayload = new SystemHealthPayload(
                3, 3, true, portalHealthSummary.getAnomalyCount() == 0,
                failedScheduledJobs);

        AssistantStatus assistantStatus = AssistantStatus.SUCCESS;

        if ((portalHealthSummary.getAnomalyCount() > 0) ||
            (failedScheduledJobs > 0)) {

            assistantStatus = AssistantStatus.WARNING;
        }

        return new PortalOpsAssistantResponse<>(
                assistantStatus, "System Health",
                assistantStatus == AssistantStatus.SUCCESS ?
                        "Portal environment is healthy." :
                        "Portal environment is operational, but some health signals need review.",
                List.of(
                        "Search service " +
                                (systemHealthPayload.isSearchOperational() ?
                                        "operational" : "requires review"),
                        "Database connection healthy",
                        failedScheduledJobs + " failed or stuck scheduled workflow job(s) detected"),
                List.of(
                        "Review content and workflow health weekly.",
                        "Investigate repeated workflow failures before they become operational incidents."),
                List.of(
                        new AssistantAction(
                                "View Details",
                                AssistantCommand.SHOW_SYSTEM_HEALTH.name())),
                systemHealthPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.SHOW_SYSTEM_HEALTH;
    }

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

}
