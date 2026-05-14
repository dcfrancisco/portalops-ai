package com.portalops.command.internal;

import com.portalops.api.audit.AuditOutcome;
import com.portalops.api.audit.AuditRecord;
import com.portalops.api.audit.AuditRecorder;
import com.portalops.api.command.CommandRouter;
import com.portalops.api.command.PortalOpsCommandIntent;
import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.command.PortalOpsCommandType;
import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.content.ContentSummary;
import com.portalops.api.permissions.PermissionFinding;
import com.portalops.api.permissions.PermissionInspectionService;
import com.portalops.api.policy.CommandAuthorizationDecision;
import com.portalops.api.policy.CommandAuthorizer;
import com.portalops.api.site.SiteFinding;
import com.portalops.api.site.SiteInspectionService;
import com.portalops.api.workflow.WorkflowInspectionService;
import com.portalops.api.workflow.WorkflowSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = CommandRouter.class)
public class PortalOpsCommandRouterComponent implements CommandRouter {

    @Override
    public PortalOpsCommandResult route(PortalOpsCommandRequest commandRequest) {
        PortalOpsCommandIntent commandIntent = _toCommandIntent(commandRequest);

        CommandAuthorizationDecision authorizationDecision = _commandAuthorizer.authorize(commandIntent);

        if (!authorizationDecision.isAllowed()) {
            _auditRecorder.record(
                    new AuditRecord(
                            commandIntent.getCommandType(),
                            commandIntent.getContext().getGroupId(),
                            authorizationDecision.getReason(), AuditOutcome.DENIED,
                            commandIntent.getContext().getUserId()));

            return new PortalOpsCommandResult(
                    commandIntent.getCommandType(), List.of(),
                    authorizationDecision.getReason(), "Command denied");
        }

        PortalOpsCommandResult commandResult = _route(commandIntent);

        _auditRecorder.record(
                new AuditRecord(
                        commandIntent.getCommandType(),
                        commandIntent.getContext().getGroupId(),
                        commandResult.getSummary(), AuditOutcome.ALLOWED,
                        commandIntent.getContext().getUserId()));

        return commandResult;
    }

    private List<String> _toContentLines(List<ContentSummary> contentSummaries) {
        List<String> lines = new ArrayList<>();

        for (ContentSummary contentSummary : contentSummaries) {
            lines.add(
                    contentSummary.getTitle() + " [" + contentSummary.getStatus() +
                            "]");
        }

        return lines;
    }

    private PortalOpsCommandIntent _toCommandIntent(
            PortalOpsCommandRequest commandRequest) {

        String normalizedCommand = commandRequest.getRawCommand();

        normalizedCommand = normalizedCommand.trim().toLowerCase(Locale.ROOT);

        PortalOpsCommandType commandType;

        switch (normalizedCommand) {
            case "/show workflows pending":
                commandType = PortalOpsCommandType.SHOW_WORKFLOWS_PENDING;
                break;
            case "/show workflows stuck":
                commandType = PortalOpsCommandType.SHOW_WORKFLOWS_STUCK;
                break;
            case "/show permissions risky":
                commandType = PortalOpsCommandType.SHOW_PERMISSIONS_RISKY;
                break;
            case "/show who can publish homepage":
                commandType = PortalOpsCommandType.SHOW_WHO_CAN_PUBLISH_HOMEPAGE;
                break;
            case "/show stale content":
                commandType = PortalOpsCommandType.SHOW_STALE_CONTENT;
                break;
            case "/show unpublished drafts":
                commandType = PortalOpsCommandType.SHOW_UNPUBLISHED_DRAFTS;
                break;
            case "/show site anomalies":
                commandType = PortalOpsCommandType.SHOW_SITE_ANOMALIES;
                break;
            case "/show orphaned pages":
                commandType = PortalOpsCommandType.SHOW_ORPHANED_PAGES;
                break;
            default:
                commandType = PortalOpsCommandType.UNSUPPORTED;
                break;
        }

        return new PortalOpsCommandIntent(
                commandType, commandRequest.getContext(),
                commandRequest.getRawCommand());
    }

    private List<String> _toPermissionLines(
            List<PermissionFinding> permissionFindings) {

        List<String> lines = new ArrayList<>();

        for (PermissionFinding permissionFinding : permissionFindings) {
            lines.add(
                    permissionFinding.getPrincipalName() + " -> " +
                            permissionFinding.getActionKey() + " on " +
                            permissionFinding.getResourceName() + " [" +
                            permissionFinding.getRiskLevel() + "]");
        }

        return lines;
    }

    private PortalOpsCommandResult _route(PortalOpsCommandIntent commandIntent) {
        switch (commandIntent.getCommandType()) {
            case SHOW_WORKFLOWS_PENDING:
                return _toWorkflowResult(
                        PortalOpsCommandType.SHOW_WORKFLOWS_PENDING,
                        _workflowInspectionService.getPendingWorkflows(
                                commandIntent.getContext()),
                        "Pending workflows");
            case SHOW_WORKFLOWS_STUCK:
                return _toWorkflowResult(
                        PortalOpsCommandType.SHOW_WORKFLOWS_STUCK,
                        _workflowInspectionService.getStuckWorkflows(
                                commandIntent.getContext()),
                        "Stuck workflows");
            case SHOW_PERMISSIONS_RISKY:
                return _toPermissionResult(
                        PortalOpsCommandType.SHOW_PERMISSIONS_RISKY,
                        _permissionInspectionService.getRiskyPermissions(
                                commandIntent.getContext()),
                        "Risky permissions");
            case SHOW_WHO_CAN_PUBLISH_HOMEPAGE:
                return _toPermissionResult(
                        PortalOpsCommandType.SHOW_WHO_CAN_PUBLISH_HOMEPAGE,
                        _permissionInspectionService.getHomepagePublishers(
                                commandIntent.getContext()),
                        "Homepage publishers");
            case SHOW_STALE_CONTENT:
                return _toContentResult(
                        PortalOpsCommandType.SHOW_STALE_CONTENT,
                        _contentInspectionService.getStaleContent(
                                commandIntent.getContext()),
                        "Stale content");
            case SHOW_UNPUBLISHED_DRAFTS:
                return _toContentResult(
                        PortalOpsCommandType.SHOW_UNPUBLISHED_DRAFTS,
                        _contentInspectionService.getUnpublishedDrafts(
                                commandIntent.getContext()),
                        "Unpublished drafts");
            case SHOW_SITE_ANOMALIES:
                return _toSiteResult(
                        PortalOpsCommandType.SHOW_SITE_ANOMALIES,
                        _siteInspectionService.getSiteAnomalies(
                                commandIntent.getContext()),
                        "Site anomalies");
            case SHOW_ORPHANED_PAGES:
                return _toSiteResult(
                        PortalOpsCommandType.SHOW_ORPHANED_PAGES,
                        _siteInspectionService.getOrphanedPages(
                                commandIntent.getContext()),
                        "Orphaned pages");
            case UNSUPPORTED:
            default:
                return new PortalOpsCommandResult(
                        PortalOpsCommandType.UNSUPPORTED, List.of(),
                        "Supported commands are modeled but not fully implemented yet.",
                        "Unsupported command");
        }
    }

    private PortalOpsCommandResult _toContentResult(
            PortalOpsCommandType commandType, List<ContentSummary> contentSummaries,
            String title) {

        return new PortalOpsCommandResult(
                commandType, _toContentLines(contentSummaries),
                "Returned " + contentSummaries.size() + " item(s).", title);
    }

    private PortalOpsCommandResult _toPermissionResult(
            PortalOpsCommandType commandType,
            List<PermissionFinding> permissionFindings, String title) {

        return new PortalOpsCommandResult(
                commandType, _toPermissionLines(permissionFindings),
                "Returned " + permissionFindings.size() + " item(s).", title);
    }

    private List<String> _toWorkflowLines(List<WorkflowSummary> workflowSummaries) {
        List<String> lines = new ArrayList<>();

        for (WorkflowSummary workflowSummary : workflowSummaries) {
            lines.add(
                    workflowSummary.getTitle() + " [" +
                            workflowSummary.getStatus() + "] - " +
                            workflowSummary.getAssigneeName());
        }

        return lines;
    }

    private List<String> _toSiteLines(List<SiteFinding> siteFindings) {
        List<String> lines = new ArrayList<>();

        for (SiteFinding siteFinding : siteFindings) {
            lines.add(
                    siteFinding.getTitle() + " [" + siteFinding.getCategory() +
                            "] - " + siteFinding.getDetail());
        }

        return lines;
    }

    private PortalOpsCommandResult _toSiteResult(
            PortalOpsCommandType commandType, List<SiteFinding> siteFindings,
            String title) {

        return new PortalOpsCommandResult(
                commandType, _toSiteLines(siteFindings),
                "Returned " + siteFindings.size() + " item(s).", title);
    }

    private PortalOpsCommandResult _toWorkflowResult(
            PortalOpsCommandType commandType, List<WorkflowSummary> workflowSummaries,
            String title) {

        return new PortalOpsCommandResult(
                commandType, _toWorkflowLines(workflowSummaries),
                "Returned " + workflowSummaries.size() + " item(s).", title);
    }

    @Reference
    private AuditRecorder _auditRecorder;

    @Reference
    private CommandAuthorizer _commandAuthorizer;

    @Reference
    private ContentInspectionService _contentInspectionService;

    @Reference
    private PermissionInspectionService _permissionInspectionService;

    @Reference
    private SiteInspectionService _siteInspectionService;

    @Reference
    private WorkflowInspectionService _workflowInspectionService;

}