package com.portalops.web.internal.dashboard;

import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.web.internal.display.DataSourceType;
import com.portalops.web.internal.display.PortalOpsAssistantData;
import com.portalops.web.internal.display.PortalOpsAssistantInsight;
import com.portalops.web.internal.display.PortalOpsDashboardCard;
import com.portalops.web.internal.display.PortalOpsDashboardData;
import com.portalops.web.internal.display.PortalOpsDashboardInsight;
import com.portalops.web.internal.display.PortalOpsDashboardQuickAction;
import com.portalops.web.internal.display.PortalOpsDashboardSection;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsDashboardDataProvider.class)
public class PortalOpsMockDashboardDataProviderComponent
        implements PortalOpsDashboardDataProvider {

    @Override
    public PortalOpsDashboardData getPortalOpsDashboardData(
            PortalOpsRequestContext portalOpsRequestContext,
            PortalKnowledgeSnapshot portalKnowledgeSnapshot) {

        int pendingDrafts = portalKnowledgeSnapshot.getContentKnowledge().
                getUnpublishedDrafts().size();
        int staleContent = portalKnowledgeSnapshot.getContentKnowledge().
                getStaleContent().size();
        int pendingWorkflows = portalKnowledgeSnapshot.getWorkflowKnowledge().
                getPendingWorkflowInspectionResult().getPendingTaskCount();
        int stuckWorkflows = portalKnowledgeSnapshot.getWorkflowKnowledge().
                getStuckWorkflows().size();
        int riskyPermissions = portalKnowledgeSnapshot.getPermissionKnowledge().
                getRiskyPermissions().size();
        int openFindings = staleContent + pendingDrafts + riskyPermissions +
                stuckWorkflows;
        int recommendedActions = 4;

        return new PortalOpsDashboardData(
                "Operational Command Center",
                "Focus on findings, governance risks, operational recommendations, " +
                        "and knowledge gaps that need administrator attention.",
                List.of(
                        new PortalOpsDashboardSection(
                                "content-intelligence", "Content Intelligence",
                                "Content findings that need governance, editorial, or lifecycle action.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Expired Content",
                                                String.valueOf(Math.max(staleContent, 9)),
                                                staleContent > 0 ? "warning" : "success",
                                                "Needs review or retirement",
                                                "Expired content should be retired, refreshed, or republished intentionally.",
                                                DataSourceType.MOCK),
                                        new PortalOpsDashboardCard(
                                                "Pending Approvals",
                                                String.valueOf(Math.max(pendingDrafts, 14)),
                                                pendingDrafts > 0 ? "warning" : "success",
                                                "Awaiting workflow decisions",
                                                "Approval queues are building in at least one site.",
                                                DataSourceType.LIVE),
                                        new PortalOpsDashboardCard(
                                                "Stale Content",
                                                String.valueOf(staleContent),
                                                staleContent > 0 ? "warning" : "success",
                                                "Low freshness signal",
                                                "These assets have not been refreshed recently and may be drifting out of policy.",
                                                DataSourceType.LIVE),
                                        new PortalOpsDashboardCard(
                                                "Content Without Owners", "11",
                                                "warning",
                                                "Governance gap",
                                                "Some high-value content lacks an identified accountable owner.",
                                                DataSourceType.COMING_SOON))),
                        new PortalOpsDashboardSection(
                                "search-intelligence", "Search Intelligence",
                                "Search issues that impact discovery quality and portal trust.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Search Failures", "8",
                                                "warning",
                                                "Last 24 hours",
                                                "Most failures were timeout-related during peak load.",
                                                DataSourceType.MOCK))),
                        new PortalOpsDashboardSection(
                                "governance-audit", "Governance & Audit",
                                "Permission, configuration, and audit findings that need review.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Permission Risks",
                                                String.valueOf(riskyPermissions),
                                                "warning",
                                                "Elevated access detected",
                                                "Review risky publish or configuration permissions before they become incidents.",
                                                DataSourceType.LIVE),
                                        new PortalOpsDashboardCard(
                                                "Configuration Changes", "5",
                                                "warning",
                                                "Last 48 hours",
                                                "Recent config edits should be reviewed before further troubleshooting.",
                                                DataSourceType.MOCK),
                                        new PortalOpsDashboardCard(
                                                "Configuration Drift", "3",
                                                "warning",
                                                "Baseline mismatch",
                                                "Deployed runtime settings no longer fully match the expected operational baseline.",
                                                DataSourceType.COMING_SOON),
                                        new PortalOpsDashboardCard(
                                                "Audit Events", String.valueOf(Math.max(pendingWorkflows, 16)),
                                                "neutral",
                                                "Security and admin actions",
                                                "Audit activity is elevated compared with the previous week.",
                                                DataSourceType.MOCK))),
                        new PortalOpsDashboardSection(
                                "operations", "Operations",
                                "Open findings and recommended next actions across PortalOps services.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Failed Jobs",
                                                String.valueOf(Math.max(stuckWorkflows, 1)),
                                                stuckWorkflows > 0 ? "critical" : "warning",
                                                "Recent scheduler failures",
                                                "At least one recurring job has failed repeatedly.",
                                                DataSourceType.LIVE),
                                        new PortalOpsDashboardCard(
                                                "Open Findings",
                                                String.valueOf(openFindings),
                                                openFindings > 0 ? "warning" : "success",
                                                "Cross-domain issues",
                                                "PortalOps has open findings across content, workflow, or governance domains.",
                                                DataSourceType.MOCK),
                                        new PortalOpsDashboardCard(
                                                "Recommended Actions",
                                                String.valueOf(recommendedActions),
                                                "neutral",
                                                "Next best actions",
                                                "PortalOps recommends a short list of operational follow-ups right now.",
                                                DataSourceType.MOCK))),
                        new PortalOpsDashboardSection(
                                "knowledge", "Knowledge",
                                "Runbook and incident knowledge gaps that reduce operational readiness.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Runbooks Requiring Review", "4",
                                                "warning",
                                                "Knowledge freshness",
                                                "Several operational runbooks have not been reviewed against recent platform changes.",
                                                DataSourceType.COMING_SOON),
                                        new PortalOpsDashboardCard(
                                                "Unresolved Operational Issues", "7",
                                                "warning",
                                                "Needs investigation",
                                                "Known operational issues remain unresolved or unassigned.",
                                                DataSourceType.COMING_SOON)))),
                List.of(
                        new PortalOpsDashboardInsight(
                                "Expiring content needs review",
                                "Expired and stale content continue to require editorial cleanup.",
                                "warning", "Review Expiring Content", "content",
                                "content-intelligence"),
                        new PortalOpsDashboardInsight(
                                "Permission review is a current governance need",
                                "Risky permissions are present and should be validated against policy expectations.",
                                "warning", "Review Permission Risks", "dashboard",
                                "governance-audit"),
                        new PortalOpsDashboardInsight(
                                "Open findings should be triaged",
                                "PortalOps has accumulated multiple open findings that need prioritization.",
                                "critical", "Show Open Findings", "dashboard",
                                "operations"),
                        new PortalOpsDashboardInsight(
                                "A recurring job is failing",
                                "One scheduled job has failed repeatedly and should be investigated.",
                                "critical", "Review Scheduled Jobs", "workflow",
                                ""),
                        new PortalOpsDashboardInsight(
                                "Knowledge gaps are emerging",
                                "Several operational runbooks require review before the next escalation cycle.",
                                "warning", "Open Knowledge Assistant", "knowledge",
                                "knowledge")),
                List.of(
                        new PortalOpsDashboardQuickAction(
                                "Show Open Findings", "warning-full", "dashboard",
                                "operations", true),
                        new PortalOpsDashboardQuickAction(
                                "View Audit Logs", "list", "audit", "", false),
                        new PortalOpsDashboardQuickAction(
                                "Review Expiring Content", "calendar", "dashboard",
                                "content-intelligence", false),
                        new PortalOpsDashboardQuickAction(
                                "Review Permission Risks", "lock", "dashboard",
                                "governance-audit", false),
                        new PortalOpsDashboardQuickAction(
                                "Review Scheduled Jobs", "time", "workflow",
                                "", false),
                        new PortalOpsDashboardQuickAction(
                                "Open Knowledge Assistant", "robot", "knowledge",
                                "", false)),
                new PortalOpsAssistantData(
                        "PortalOps Assistant",
                        "Ask PortalOps about your portal.",
                        "Ask PortalOps...",
                        "Analyze",
                        List.of(
                                "Show System Health", "Show Stale Content",
                                "Analyze Search Health",
                                "Review Permission Risks",
                                "Show Recent Changes",
                                "Show Failed Workflows"),
                        new PortalOpsAssistantInsight(
                                "PortalOps Insight", "Information",
                                List.of(
                                        "Select a supported PortalOps command to inspect operational intelligence.",
                                        "Phase 1 executes deterministic handlers backed by PortalOps services.",
                                        "No LLM is required for assistant execution."),
                                List.of(
                                        "Start with System Health or Stale Content.",
                                        "Use structured responses to guide follow-up actions."))));
    }

}
