package com.portalops.permissions.internal;

import com.portalops.api.permissions.PermissionFinding;
import com.portalops.api.permissions.PermissionInspectionService;
import com.portalops.api.service.PortalOpsRequestContext;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PermissionInspectionService.class)
public class PortalOpsPermissionInspectionServiceComponent
        implements PermissionInspectionService {

    @Override
    public List<PermissionFinding> getHomepagePublishers(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

    @Override
    public List<PermissionFinding> getRiskyPermissions(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

}