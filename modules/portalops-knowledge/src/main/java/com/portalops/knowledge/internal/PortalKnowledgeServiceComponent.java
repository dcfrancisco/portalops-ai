package com.portalops.knowledge.internal;

import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.knowledge.ContentKnowledge;
import com.portalops.api.knowledge.PermissionKnowledge;
import com.portalops.api.knowledge.PortalHealthSummary;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.knowledge.SiteKnowledge;
import com.portalops.api.knowledge.WorkflowKnowledge;
import com.portalops.api.permissions.PermissionInspectionService;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.site.SiteInspectionService;
import com.portalops.api.workflow.WorkflowInspectionService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PortalKnowledgeService.class)
public class PortalKnowledgeServiceComponent implements PortalKnowledgeService {

    @Override
    public PortalKnowledgeSnapshot getSnapshot(PortalOpsRequestContext context) {
        WorkflowKnowledge workflowKnowledge = new WorkflowKnowledge(
                _workflowInspectionService.getPendingWorkflows(context),
                _workflowInspectionService.getStuckWorkflows(context));

        PermissionKnowledge permissionKnowledge = new PermissionKnowledge(
                _permissionInspectionService.getHomepagePublishers(context),
                _permissionInspectionService.getRiskyPermissions(context));

        ContentKnowledge contentKnowledge = new ContentKnowledge(
                _contentInspectionService.getStaleContent(context),
                _contentInspectionService.getUnpublishedDrafts(context));

        SiteKnowledge siteKnowledge = new SiteKnowledge(
                _siteInspectionService.getOrphanedPages(context),
                _siteInspectionService.getSiteAnomalies(context));

        PortalHealthSummary portalHealthSummary = new PortalHealthSummary(
                siteKnowledge.getSiteAnomalies().size(),
                siteKnowledge.getOrphanedPages().size(),
                workflowKnowledge.getPendingWorkflows().size(),
                permissionKnowledge.getRiskyPermissions().size(),
                contentKnowledge.getStaleContent().size());

        return new PortalKnowledgeSnapshot(
                contentKnowledge, permissionKnowledge, portalHealthSummary,
                siteKnowledge, workflowKnowledge);
    }

    @Reference
    private ContentInspectionService _contentInspectionService;

    @Reference
    private PermissionInspectionService _permissionInspectionService;

    @Reference
    private SiteInspectionService _siteInspectionService;

    @Reference
    private WorkflowInspectionService _workflowInspectionService;

}