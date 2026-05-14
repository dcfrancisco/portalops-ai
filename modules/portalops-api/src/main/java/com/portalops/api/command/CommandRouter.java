package com.portalops.api.command;

public interface CommandRouter {

    public PortalOpsCommandResult route(PortalOpsCommandRequest commandRequest);

}