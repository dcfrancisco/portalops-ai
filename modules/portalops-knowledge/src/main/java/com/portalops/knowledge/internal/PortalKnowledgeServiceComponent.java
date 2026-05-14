package com.portalops.knowledge.internal;

import com.portalops.api.knowledge.ContentKnowledge;
import com.portalops.api.knowledge.PermissionKnowledge;
import com.portalops.api.knowledge.PortalHealthSummary;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.knowledge.SiteKnowledge;
import com.portalops.api.knowledge.WorkflowKnowledge;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.workflow.WorkflowInspectionResult;
import com.portalops.api.workflow.WorkflowInspectionService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PortalKnowledgeService.class)
public class PortalKnowledgeServiceComponent implements PortalKnowledgeService {

    @Override
    public PortalKnowledgeSnapshot getSnapshot(PortalOpsRequestContext context) {
        WorkflowInspectionResult workflowInspectionResult = _workflowInspectionService.inspectPendingWorkflows(context);

        WorkflowKnowledge workflowKnowledge = new WorkflowKnowledge(
                workflowInspectionResult,
                _workflowInspectionService.getStuckWorkflows(context));

        PermissionKnowledge permissionKnowledge = new PermissionKnowledge(
                List.of(), List.of());

        ContentKnowledge contentKnowledge = new ContentKnowledge(
                List.of(), List.of());

        SiteKnowledge siteKnowledge = new SiteKnowledge(List.of(), List.of());

        PortalHealthSummary portalHealthSummary = new PortalHealthSummary(
                0,
                0,
                workflowInspectionResult.getPendingTaskCount(),
                0,
                0);

        return new PortalKnowledgeSnapshot(
                contentKnowledge, permissionKnowledge, portalHealthSummary,
                siteKnowledge, workflowKnowledge);
    }

    @Reference
    private WorkflowInspectionService _workflowInspectionService;

}