package com.portalops.service.internal;

import com.portalops.api.command.CommandRouter;
import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.service.PortalOpsFacade;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.workflow.WorkflowInspectionResult;
import com.portalops.api.workflow.WorkflowInspectionService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PortalOpsFacade.class)
public class PortalOpsFacadeComponent implements PortalOpsFacade {

    @Override
    public PortalOpsCommandResult execute(PortalOpsCommandRequest commandRequest) {
        return _commandRouter.route(commandRequest);
    }

    @Override
    public WorkflowInspectionResult inspectPendingWorkflows(
            PortalOpsRequestContext context) {

        return _workflowInspectionService.inspectPendingWorkflows(context);
    }

    @Override
    public PortalKnowledgeSnapshot getPortalKnowledgeSnapshot(
            PortalOpsRequestContext context) {

        return _portalKnowledgeService.getSnapshot(context);
    }

    @Reference
    private CommandRouter _commandRouter;

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

    @Reference
    private WorkflowInspectionService _workflowInspectionService;

}