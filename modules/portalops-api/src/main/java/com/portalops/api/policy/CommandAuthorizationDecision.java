package com.portalops.api.policy;

import java.io.Serializable;

public class CommandAuthorizationDecision implements Serializable {

    public static CommandAuthorizationDecision allow() {
        return new CommandAuthorizationDecision(true, "");
    }

    public static CommandAuthorizationDecision deny(String reason) {
        return new CommandAuthorizationDecision(false, reason);
    }

    public CommandAuthorizationDecision(boolean allowed, String reason) {
        _allowed = allowed;
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public boolean isAllowed() {
        return _allowed;
    }

    private final boolean _allowed;
    private final String _reason;

}