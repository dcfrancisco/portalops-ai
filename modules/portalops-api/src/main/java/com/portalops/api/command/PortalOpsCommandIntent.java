package com.portalops.api.command;

import com.portalops.api.service.PortalOpsRequestContext;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsCommandIntent implements Serializable {

    public PortalOpsCommandIntent(
            PortalOpsCommandType commandType, PortalOpsRequestContext context,
            String rawCommand) {

        _commandType = Objects.requireNonNull(commandType);
        _context = Objects.requireNonNull(context);
        _rawCommand = Objects.requireNonNull(rawCommand);
    }

    public PortalOpsCommandType getCommandType() {
        return _commandType;
    }

    public PortalOpsRequestContext getContext() {
        return _context;
    }

    public String getRawCommand() {
        return _rawCommand;
    }

    private final PortalOpsCommandType _commandType;
    private final PortalOpsRequestContext _context;
    private final String _rawCommand;

}