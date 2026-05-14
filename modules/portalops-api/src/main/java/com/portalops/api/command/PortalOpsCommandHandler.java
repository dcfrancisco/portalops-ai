package com.portalops.api.command;

public interface PortalOpsCommandHandler {

    public PortalOpsCommandResult handle(PortalOpsCommandIntent commandIntent);

    public boolean supports(PortalOpsCommandType commandType);

}