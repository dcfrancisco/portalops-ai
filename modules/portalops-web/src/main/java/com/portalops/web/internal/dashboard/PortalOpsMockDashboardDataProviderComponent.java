package com.portalops.web.internal.dashboard;

import com.portalops.api.knowledge.PortalHealthSummary;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.service.PortalOpsRequestContext;
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

        PortalHealthSummary portalHealthSummary =
                portalKnowledgeSnapshot.getPortalHealthSummary();

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

        return new PortalOpsDashboardData(
                "Operational Command Center",
                "Focus on active risks, stale content, search drift, and " +
                        "access issues that need an administrator's attention.",
                List.of(
                        new PortalOpsDashboardSection(
                                "environment-health", "Environment Health",
                                "Runtime stability and dependency health across " +
                                        "the current portal environment.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Portal Status",
                                                _getPortalStatus(portalHealthSummary),
                                                _getPortalStatusType(
                                                        portalHealthSummary),
                                                "Overall environment posture",
                                                _getPortalStatusNote(
                                                        portalHealthSummary)),
                                        new PortalOpsDashboardCard(
                                                "Cluster Nodes", "3 / 3",
                                                "success",
                                                "All nodes reporting",
                                                "No cluster partition or heartbeat loss detected."),
                                        new PortalOpsDashboardCard(
                                                "Active Users", "248",
                                                "neutral",
                                                "Current authenticated sessions",
                                                "Within the normal weekday operating range."),
                                        new PortalOpsDashboardCard(
                                                "JVM Memory Usage", "72%",
                                                "warning",
                                                "Heap utilization",
                                                "Usage is elevated but remains below the alert threshold."),
                                        new PortalOpsDashboardCard(
                                                "Database Connectivity", "Healthy",
                                                "success",
                                                "Primary datasource",
                                                "Connection pool latency is stable with no recent failures."),
                                        new PortalOpsDashboardCard(
                                                "Search Engine Status", "Warning",
                                                "warning",
                                                "Search cluster sync",
                                                "Index updates are trailing recent content changes."))),
                        new PortalOpsDashboardSection(
                                "content-intelligence", "Content Intelligence",
                                "Content that is stale, scheduled, broken, or " +
                                        "waiting for editorial action.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Expired Web Content",
                                                String.valueOf(Math.max(staleContent, 9)),
                                                staleContent > 0 ? "warning" : "success",
                                                "Needs review or retirement",
                                                "Items are still visible in operational reporting despite expiration."),
                                        new PortalOpsDashboardCard(
                                                "Scheduled Publications", "18",
                                                "neutral",
                                                "Next 14 days",
                                                "A concentrated publishing window is approaching this week."),
                                        new PortalOpsDashboardCard(
                                                "Broken Content References", "6",
                                                "critical",
                                                "Fragments, pages, or assets",
                                                "Broken references may cause incomplete page rendering."),
                                        new PortalOpsDashboardCard(
                                                "Draft Content Pending Approval",
                                                String.valueOf(Math.max(pendingDrafts, 14)),
                                                pendingDrafts > 0 ? "warning" : "success",
                                                "Awaiting workflow decisions",
                                                "Approval queues are building in at least one site."),
                                        new PortalOpsDashboardCard(
                                                "Recently Modified Content", "42",
                                                "neutral",
                                                "Updated in the last 24 hours",
                                                "Recent change volume suggests reviewing search freshness."))),
                        new PortalOpsDashboardSection(
                                "search-intelligence", "Search Intelligence",
                                "Signals that show when discovery quality is slipping.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Unindexed Content", "27",
                                                "critical",
                                                "Content newer than index coverage",
                                                "These items may not appear in search until indexing catches up."),
                                        new PortalOpsDashboardCard(
                                                "Reindex Recommendations", "1",
                                                "warning",
                                                "Recommended actions",
                                                "A scoped reindex is recommended for Web Content and Documents."),
                                        new PortalOpsDashboardCard(
                                                "Search Failures", "8",
                                                "warning",
                                                "Last 24 hours",
                                                "Most failures were timeout-related during peak load."),
                                        new PortalOpsDashboardCard(
                                                "Top Search Terms", "Pricing, Careers",
                                                "neutral",
                                                "Most common queries",
                                                "High-interest topics can guide content and synonym tuning."),
                                        new PortalOpsDashboardCard(
                                                "Zero Result Searches", "11%",
                                                "warning",
                                                "Share of failed discovery",
                                                "Refine mappings or create content for repeated no-result terms."))),
                        new PortalOpsDashboardSection(
                                "user-security", "User & Security",
                                "Identity, access, and account signals that deserve review.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Failed Login Attempts", "37",
                                                "warning",
                                                "Last 24 hours",
                                                "Failed attempts are above baseline for this environment."),
                                        new PortalOpsDashboardCard(
                                                "Locked Accounts", "4",
                                                "warning",
                                                "Requires admin review",
                                                "Confirm whether lockouts are user error or suspicious activity."),
                                        new PortalOpsDashboardCard(
                                                "Expiring Passwords", "19",
                                                "neutral",
                                                "Within 7 days",
                                                "Password expiry reminders should be sent to affected users."),
                                        new PortalOpsDashboardCard(
                                                "Inactive Users", "12",
                                                "warning",
                                                "No login in 180 days",
                                                "Review for deprovisioning or access cleanup."),
                                        new PortalOpsDashboardCard(
                                                "Service Account Activity", "Normal",
                                                riskyPermissions > 0 ? "warning" : "success",
                                                "Scheduled integrations",
                                                riskyPermissions > 0 ?
                                                        "Review elevated service account permissions." :
                                                        "No abnormal spikes detected in service account activity."))),
                        new PortalOpsDashboardSection(
                                "system-operations", "System Operations",
                                "Operational work queues, job failures, deployment activity, and audit change signals.",
                                List.of(
                                        new PortalOpsDashboardCard(
                                                "Long Running Jobs", "2",
                                                "warning",
                                                "Running longer than expected",
                                                "Two maintenance tasks exceeded their normal duration."),
                                        new PortalOpsDashboardCard(
                                                "Failed Scheduled Jobs",
                                                String.valueOf(Math.max(stuckWorkflows, 1)),
                                                stuckWorkflows > 0 ? "critical" : "warning",
                                                "Recent scheduler failures",
                                                "At least one recurring job has failed repeatedly."),
                                        new PortalOpsDashboardCard(
                                                "Recent Deployments", "3",
                                                "neutral",
                                                "Last 7 days",
                                                "Use deployment timing to correlate with new incidents."),
                                        new PortalOpsDashboardCard(
                                                "Configuration Changes", "5",
                                                "warning",
                                                "Last 48 hours",
                                                "Recent config edits should be reviewed before further troubleshooting."),
                                        new PortalOpsDashboardCard(
                                                "Audit Events", String.valueOf(Math.max(pendingWorkflows, 16)),
                                                "neutral",
                                                "Security and admin actions",
                                                "Audit activity is elevated compared with the previous week.")))),
                List.of(
                        new PortalOpsDashboardInsight(
                                "Expiring content needs review",
                                "34 web contents are scheduled to expire within 7 days.",
                                "warning", "Review Expiring Content", "content",
                                "content-intelligence"),
                        new PortalOpsDashboardInsight(
                                "Search freshness is lagging",
                                "Search index is 3 days behind the latest content updates.",
                                "critical", "Reindex Search", "dashboard",
                                "search-intelligence"),
                        new PortalOpsDashboardInsight(
                                "Inactive accounts can be cleaned up",
                                "12 users have not logged in for 180 days.",
                                "warning", "Manage Users", "dashboard",
                                "user-security"),
                        new PortalOpsDashboardInsight(
                                "A recurring job is failing",
                                "One scheduled job has failed repeatedly and should be investigated.",
                                "critical", "Review Scheduled Jobs", "workflow",
                                ""),
                        new PortalOpsDashboardInsight(
                                "Draft backlog is concentrated in Marketing",
                                "Site Marketing contains 14 unpublished drafts pending review.",
                                "warning", "Open Knowledge Assistant", "knowledge",
                                "")),
                List.of(
                        new PortalOpsDashboardQuickAction(
                                "Reindex Search", "reload", "dashboard",
                                "search-intelligence", true),
                        new PortalOpsDashboardQuickAction(
                                "View Audit Logs", "list", "audit", "", false),
                        new PortalOpsDashboardQuickAction(
                                "Review Expiring Content", "calendar", "content",
                                "content-intelligence", false),
                        new PortalOpsDashboardQuickAction(
                                "Manage Users", "users", "dashboard",
                                "user-security", false),
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

    private String _getPortalStatus(PortalHealthSummary portalHealthSummary) {
        if (portalHealthSummary.getAnomalyCount() > 3) {
            return "Critical";
        }

        if (portalHealthSummary.getAnomalyCount() > 0) {
            return "Warning";
        }

        return "Healthy";
    }

    private String _getPortalStatusNote(
            PortalHealthSummary portalHealthSummary) {

        if (portalHealthSummary.getAnomalyCount() > 3) {
            return "Multiple anomalies are affecting operational confidence.";
        }

        if (portalHealthSummary.getAnomalyCount() > 0) {
            return "Some operational anomalies were detected and need review.";
        }

        return "No active anomaly patterns are reported by current knowledge sources.";
    }

    private String _getPortalStatusType(
            PortalHealthSummary portalHealthSummary) {

        if (portalHealthSummary.getAnomalyCount() > 3) {
            return "critical";
        }

        if (portalHealthSummary.getAnomalyCount() > 0) {
            return "warning";
        }

        return "success";
    }

}
