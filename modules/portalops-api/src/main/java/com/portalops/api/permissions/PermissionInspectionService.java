package com.portalops.api.permissions;

import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

public interface PermissionInspectionService {

    public List<PermissionFinding> getHomepagePublishers(
            PortalOpsRequestContext context);

    public List<PermissionFinding> getRiskyPermissions(
            PortalOpsRequestContext context);

}