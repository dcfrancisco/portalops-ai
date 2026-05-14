package com.portalops.site.internal;

import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.site.SiteFinding;
import com.portalops.api.site.SiteInspectionService;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = SiteInspectionService.class)
public class PortalOpsSiteInspectionServiceComponent
        implements SiteInspectionService {

    @Override
    public List<SiteFinding> getOrphanedPages(PortalOpsRequestContext context) {
        return Collections.emptyList();
    }

    @Override
    public List<SiteFinding> getSiteAnomalies(PortalOpsRequestContext context) {
        return Collections.emptyList();
    }

}