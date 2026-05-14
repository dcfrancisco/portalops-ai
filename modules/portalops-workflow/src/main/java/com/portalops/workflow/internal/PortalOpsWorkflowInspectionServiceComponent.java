package com.portalops.workflow.internal;

import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.workflow.WorkflowInspectionService;
import com.portalops.api.workflow.WorkflowSummary;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = WorkflowInspectionService.class)
public class PortalOpsWorkflowInspectionServiceComponent
        implements WorkflowInspectionService {

    @Override
    public List<WorkflowSummary> getPendingWorkflows(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

    @Override
    public List<WorkflowSummary> getStuckWorkflows(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

}