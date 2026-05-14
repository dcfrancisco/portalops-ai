package com.portalops.api.command;

import com.portalops.api.service.PortalOpsRequestContext;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsCommandRequest implements Serializable {

    public PortalOpsCommandRequest(
            PortalOpsRequestContext context, String rawCommand) {

        _context = Objects.requireNonNull(context);
        _rawCommand = Objects.requireNonNull(rawCommand);
    }

    public PortalOpsRequestContext getContext() {
        return _context;
    }

    public String getRawCommand() {
        return _rawCommand;
    }

    private final PortalOpsRequestContext _context;
    private final String _rawCommand;

}