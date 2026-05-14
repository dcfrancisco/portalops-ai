package com.portalops.policy.internal;

import com.portalops.api.command.PortalOpsCommandIntent;
import com.portalops.api.command.PortalOpsCommandType;
import com.portalops.api.policy.CommandAuthorizationDecision;
import com.portalops.api.policy.CommandAuthorizer;

import org.osgi.service.component.annotations.Component;

@Component(service = CommandAuthorizer.class)
public class PortalOpsCommandAuthorizerComponent implements CommandAuthorizer {

    @Override
    public CommandAuthorizationDecision authorize(
            PortalOpsCommandIntent commandIntent) {

        if (commandIntent.getCommandType() == PortalOpsCommandType.UNSUPPORTED) {
            return CommandAuthorizationDecision.deny(
                    "Command is not yet modeled in the MVP scaffold.");
        }

        return CommandAuthorizationDecision.allow();
    }

}