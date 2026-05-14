package com.portalops.api.knowledge;

import java.io.Serializable;
import java.util.Objects;

public class PortalKnowledgeSnapshot implements Serializable {

    public PortalKnowledgeSnapshot(ContentKnowledge contentKnowledge,
            PermissionKnowledge permissionKnowledge,
            PortalHealthSummary portalHealthSummary, SiteKnowledge siteKnowledge,
            WorkflowKnowledge workflowKnowledge) {

        _contentKnowledge = Objects.requireNonNull(contentKnowledge);
        _permissionKnowledge = Objects.requireNonNull(permissionKnowledge);
        _portalHealthSummary = Objects.requireNonNull(portalHealthSummary);
        _siteKnowledge = Objects.requireNonNull(siteKnowledge);
        _workflowKnowledge = Objects.requireNonNull(workflowKnowledge);
    }

    public ContentKnowledge getContentKnowledge() {
        return _contentKnowledge;
    }

    public PermissionKnowledge getPermissionKnowledge() {
        return _permissionKnowledge;
    }

    public PortalHealthSummary getPortalHealthSummary() {
        return _portalHealthSummary;
    }

    public SiteKnowledge getSiteKnowledge() {
        return _siteKnowledge;
    }

    public WorkflowKnowledge getWorkflowKnowledge() {
        return _workflowKnowledge;
    }

    private final ContentKnowledge _contentKnowledge;
    private final PermissionKnowledge _permissionKnowledge;
    private final PortalHealthSummary _portalHealthSummary;
    private final SiteKnowledge _siteKnowledge;
    private final WorkflowKnowledge _workflowKnowledge;

}