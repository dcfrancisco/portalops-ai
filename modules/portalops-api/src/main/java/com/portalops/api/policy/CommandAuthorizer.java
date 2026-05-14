package com.portalops.api.policy;

import com.portalops.api.command.PortalOpsCommandIntent;

public interface CommandAuthorizer {

    public CommandAuthorizationDecision authorize(
            PortalOpsCommandIntent commandIntent);

}