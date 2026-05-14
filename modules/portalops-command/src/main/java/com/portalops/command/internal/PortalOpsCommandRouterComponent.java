package com.portalops.command.internal;

import com.portalops.api.audit.AuditOutcome;
import com.portalops.api.audit.AuditRecord;
import com.portalops.api.audit.AuditRecorder;
import com.portalops.api.command.CommandRouter;
import com.portalops.api.command.PortalOpsCommandHandler;
import com.portalops.api.command.PortalOpsCommandIntent;
import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.command.PortalOpsCommandType;
import com.portalops.api.policy.CommandAuthorizationDecision;
import com.portalops.api.policy.CommandAuthorizer;

import java.util.Collection;
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

    private PortalOpsCommandResult _route(PortalOpsCommandIntent commandIntent) {
        for (PortalOpsCommandHandler commandHandler : _portalOpsCommandHandlers) {
            if (commandHandler.supports(commandIntent.getCommandType())) {
                return commandHandler.handle(commandIntent);
            }
        }

        return new PortalOpsCommandResult(
                PortalOpsCommandType.UNSUPPORTED, List.of(),
                "Phase 0 only scaffolds command routing and the pending workflow handler.",
                "Unsupported command");
    }

    @Reference
    private AuditRecorder _auditRecorder;

    @Reference
    private CommandAuthorizer _commandAuthorizer;

    @Reference
    private Collection<PortalOpsCommandHandler> _portalOpsCommandHandlers;

}