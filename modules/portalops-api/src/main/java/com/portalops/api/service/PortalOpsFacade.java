package com.portalops.api.service;

import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;

public interface PortalOpsFacade {

    public PortalOpsCommandResult execute(PortalOpsCommandRequest commandRequest);

}