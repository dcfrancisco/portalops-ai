package com.portalops.api.site;

import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

public interface SiteInspectionService {

    public java.util.List<SiteSummary> getSiteActivity(
            PortalOpsRequestContext context);

    public java.util.List<SiteSummary> getSiteMembership(
            PortalOpsRequestContext context);

    public java.util.List<SiteSummary> getSites(PortalOpsRequestContext context);

    public List<SiteFinding> getOrphanedPages(PortalOpsRequestContext context);

    public List<SiteFinding> getSiteAnomalies(PortalOpsRequestContext context);

}
