package com.portalops.command.internal.handler;

import com.portalops.api.command.PortalOpsCommandHandler;
import com.portalops.api.command.PortalOpsCommandIntent;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.command.PortalOpsCommandType;
import com.portalops.api.workflow.WorkflowInspectionResult;
import com.portalops.api.workflow.WorkflowPendingSummary;
import com.portalops.api.workflow.WorkflowInspectionService;
import com.portalops.api.workflow.WorkflowTaskSummary;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PortalOpsCommandHandler.class)
public class ShowPendingWorkflowsCommandHandlerComponent
        implements PortalOpsCommandHandler {

    @Override
    public PortalOpsCommandResult handle(PortalOpsCommandIntent commandIntent) {
        WorkflowInspectionResult workflowInspectionResult = _workflowInspectionService.inspectPendingWorkflows(
                commandIntent.getContext());

        return new PortalOpsCommandResult(
                PortalOpsCommandType.SHOW_WORKFLOWS_PENDING,
                _toLines(workflowInspectionResult),
                "Returned " + workflowInspectionResult.getPendingTaskCount() +
                        " pending workflow task(s) in scope " +
                        workflowInspectionResult.getScopeGroupName() + ".",
                "Pending workflows");
    }

    @Override
    public boolean supports(PortalOpsCommandType commandType) {
        return commandType == PortalOpsCommandType.SHOW_WORKFLOWS_PENDING;
    }

    private List<String> _toLines(WorkflowInspectionResult workflowInspectionResult) {
        List<String> lines = new ArrayList<>();

        lines.add("Scope: " + workflowInspectionResult.getScopeGroupName());
        lines.add("By assignee:");

        for (WorkflowPendingSummary workflowPendingSummary : workflowInspectionResult.getPendingByAssignee()) {

            lines.add(
                    "  " + workflowPendingSummary.getLabel() + ": " +
                            workflowPendingSummary.getTaskCount());
        }

        lines.add("By workflow definition:");

        for (WorkflowPendingSummary workflowPendingSummary : workflowInspectionResult
                .getPendingByWorkflowDefinition()) {

            lines.add(
                    "  " + workflowPendingSummary.getLabel() + ": " +
                            workflowPendingSummary.getTaskCount());
        }

        lines.add("Pending tasks:");

        for (WorkflowTaskSummary workflowTaskSummary : workflowInspectionResult.getWorkflowTaskSummaries()) {

            lines.add(
                    "  " + workflowTaskSummary.getAssetTitle() + " [" +
                            workflowTaskSummary.getWorkflowTaskName() + "] - " +
                            workflowTaskSummary.getAssigneeName() + " (" +
                            workflowTaskSummary.getWorkflowDefinitionLabel() + ")");
        }

        return lines;
    }

    @Reference
    private WorkflowInspectionService _workflowInspectionService;

}