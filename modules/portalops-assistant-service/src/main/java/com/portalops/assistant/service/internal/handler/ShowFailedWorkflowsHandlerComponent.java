package com.portalops.assistant.service.internal.handler;

import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.workflow.WorkflowSummary;
import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.FailedWorkflowItem;
import com.portalops.assistant.api.payload.FailedWorkflowPayload;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandHandler.class)
public class ShowFailedWorkflowsHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<FailedWorkflowPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        PortalKnowledgeSnapshot portalKnowledgeSnapshot =
                _portalKnowledgeService.getSnapshot(
                        portalOpsAssistantRequest.getContext());

        List<FailedWorkflowItem> failedWorkflowItems = new ArrayList<>();

        for (WorkflowSummary workflowSummary :
                portalKnowledgeSnapshot.getWorkflowKnowledge().
                        getStuckWorkflows()) {

            failedWorkflowItems.add(
                    new FailedWorkflowItem(
                            workflowSummary.getTitle(),
                            workflowSummary.getAssigneeName(),
                            workflowSummary.getStatus()));
        }

        FailedWorkflowPayload failedWorkflowPayload =
                new FailedWorkflowPayload(failedWorkflowItems);

        return new PortalOpsAssistantResponse<>(
                failedWorkflowItems.isEmpty() ? AssistantStatus.INFO :
                        AssistantStatus.WARNING,
                "Failed Workflows",
                failedWorkflowItems.isEmpty() ?
                        "No failed workflows were detected." :
                        "PortalOps found workflows that may require intervention.",
                List.of(
                        failedWorkflowItems.size() +
                                " workflow issue(s) matched the current failed or stuck criteria."),
                List.of(
                        "Review workflow definitions with repeated failures.",
                        "Escalate stuck approvals to the relevant content owners."),
                List.of(
                        new AssistantAction(
                                "Show Failed Workflows",
                                AssistantCommand.SHOW_FAILED_WORKFLOWS.name())),
                failedWorkflowPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.SHOW_FAILED_WORKFLOWS;
    }

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

}
