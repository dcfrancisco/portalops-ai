package com.portalops.api.service;

import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.workflow.WorkflowInspectionResult;

public interface PortalOpsFacade {

    public PortalOpsCommandResult execute(PortalOpsCommandRequest commandRequest);

    public WorkflowInspectionResult inspectPendingWorkflows(
            PortalOpsRequestContext context);

    public PortalKnowledgeSnapshot getPortalKnowledgeSnapshot(
            PortalOpsRequestContext context);

}