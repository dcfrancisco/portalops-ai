package com.portalops.api.permissions;

import java.io.Serializable;
import java.util.Objects;

public class PermissionFinding implements Serializable {

    public PermissionFinding(
            String actionKey, String principalName, String resourceName,
            String riskLevel) {

        _actionKey = Objects.requireNonNull(actionKey);
        _principalName = Objects.requireNonNull(principalName);
        _resourceName = Objects.requireNonNull(resourceName);
        _riskLevel = Objects.requireNonNull(riskLevel);
    }

    public String getActionKey() {
        return _actionKey;
    }

    public String getPrincipalName() {
        return _principalName;
    }

    public String getResourceName() {
        return _resourceName;
    }

    public String getRiskLevel() {
        return _riskLevel;
    }

    private final String _actionKey;
    private final String _principalName;
    private final String _resourceName;
    private final String _riskLevel;

}