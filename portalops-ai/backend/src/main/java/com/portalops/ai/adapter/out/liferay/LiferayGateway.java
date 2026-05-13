package com.portalops.ai.adapter.out.liferay;

import com.portalops.ai.domain.content.ContentFinding;
import com.portalops.ai.domain.permissions.PermissionRisk;
import com.portalops.ai.domain.workflow.WorkflowSummary;

import java.util.List;
import java.util.Map;

public interface LiferayGateway {

    List<WorkflowSummary> loadPendingWorkflows();

    List<PermissionRisk> loadPermissionRisks();

    List<ContentFinding> loadStaleContent();

    Map<String, Object> loadPortalOverview();
}