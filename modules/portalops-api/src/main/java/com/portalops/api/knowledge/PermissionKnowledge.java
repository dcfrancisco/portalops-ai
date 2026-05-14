package com.portalops.api.knowledge;

import com.portalops.api.permissions.PermissionFinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionKnowledge implements Serializable {

    public PermissionKnowledge(List<PermissionFinding> homepagePublishers,
            List<PermissionFinding> riskyPermissions) {

        _homepagePublishers = Collections.unmodifiableList(
                new ArrayList<>(homepagePublishers));
        _riskyPermissions = Collections.unmodifiableList(
                new ArrayList<>(riskyPermissions));
    }

    public List<PermissionFinding> getHomepagePublishers() {
        return _homepagePublishers;
    }

    public List<PermissionFinding> getRiskyPermissions() {
        return _riskyPermissions;
    }

    private final List<PermissionFinding> _homepagePublishers;
    private final List<PermissionFinding> _riskyPermissions;

}