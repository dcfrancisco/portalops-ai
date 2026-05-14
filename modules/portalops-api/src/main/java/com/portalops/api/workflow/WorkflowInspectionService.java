package com.portalops.api.workflow;

import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

public interface WorkflowInspectionService {

    public WorkflowInspectionResult inspectPendingWorkflows(
            PortalOpsRequestContext context);

    public List<WorkflowSummary> getPendingWorkflows(
            PortalOpsRequestContext context);

    public List<WorkflowSummary> getStuckWorkflows(
            PortalOpsRequestContext context);

}