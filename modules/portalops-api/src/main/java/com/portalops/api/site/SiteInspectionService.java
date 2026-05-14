package com.portalops.api.site;

import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

public interface SiteInspectionService {

    public List<SiteFinding> getOrphanedPages(PortalOpsRequestContext context);

    public List<SiteFinding> getSiteAnomalies(PortalOpsRequestContext context);

}