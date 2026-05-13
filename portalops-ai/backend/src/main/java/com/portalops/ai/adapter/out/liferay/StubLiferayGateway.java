package com.portalops.ai.adapter.out.liferay;

import com.portalops.ai.domain.content.ContentFinding;
import com.portalops.ai.domain.permissions.PermissionRisk;
import com.portalops.ai.domain.workflow.WorkflowSummary;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class StubLiferayGateway implements LiferayGateway {

    @Override
    public List<WorkflowSummary> loadPendingWorkflows() {
        return List.of(
                new WorkflowSummary("Homepage refresh", "Site Approver", "pending"),
                new WorkflowSummary("Policy update article", "Legal Reviewer", "stalled"));
    }

    @Override
    public List<PermissionRisk> loadPermissionRisks() {
        return List.of(
                new PermissionRisk("Global Site", "high", "Guest role can publish web content"),
                new PermissionRisk("Executive Pages", "medium", "Multiple custom roles can update navigation"));
    }

    @Override
    public List<ContentFinding> loadStaleContent() {
        return List.of(
                new ContentFinding("Q2 campaign banner", "draft", "Review or archive after campaign close"),
                new ContentFinding("Legacy FAQ page", "orphaned", "Reconnect to navigation or retire"));
    }

    @Override
    public Map<String, Object> loadPortalOverview() {
        return Map.of(
                "openAlerts", 3,
                "sitesMonitored", 12,
                "staleAssets", 17,
                "workflowBacklog", 5);
    }
}