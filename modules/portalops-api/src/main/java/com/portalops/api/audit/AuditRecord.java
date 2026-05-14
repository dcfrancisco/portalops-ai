package com.portalops.api.audit;

import com.portalops.api.command.PortalOpsCommandType;

import java.io.Serializable;
import java.util.Objects;

public class AuditRecord implements Serializable {

    public AuditRecord(
            PortalOpsCommandType commandType, long groupId, String message,
            AuditOutcome outcome, long userId) {

        _commandType = Objects.requireNonNull(commandType);
        _groupId = groupId;
        _message = Objects.requireNonNull(message);
        _outcome = Objects.requireNonNull(outcome);
        _userId = userId;
    }

    public PortalOpsCommandType getCommandType() {
        return _commandType;
    }

    public long getGroupId() {
        return _groupId;
    }

    public String getMessage() {
        return _message;
    }

    public AuditOutcome getOutcome() {
        return _outcome;
    }

    public long getUserId() {
        return _userId;
    }

    private final PortalOpsCommandType _commandType;
    private final long _groupId;
    private final String _message;
    private final AuditOutcome _outcome;
    private final long _userId;

}