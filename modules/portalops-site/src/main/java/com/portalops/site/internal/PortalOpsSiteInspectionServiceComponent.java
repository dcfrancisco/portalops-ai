package com.portalops.site.internal;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.site.SiteFinding;
import com.portalops.api.site.SiteInspectionService;
import com.portalops.api.site.SiteSummary;

import java.time.Instant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = SiteInspectionService.class)
public class PortalOpsSiteInspectionServiceComponent
        implements SiteInspectionService {

    @Override
    public List<SiteSummary> getSiteActivity(PortalOpsRequestContext context) {
        List<SiteSummary> siteSummaries = _getSiteSummaries(context);

        siteSummaries.sort(
                Comparator.comparing(
                        SiteSummary::getModifiedDate,
                        Comparator.nullsLast(String::compareTo))
                .reversed());

        return List.copyOf(siteSummaries);
    }

    @Override
    public List<SiteSummary> getSiteMembership(PortalOpsRequestContext context) {
        List<SiteSummary> siteSummaries = _getSiteSummaries(context);

        siteSummaries.sort(
                Comparator.comparingInt(
                        SiteSummary::getUserCount
                ).reversed());

        return List.copyOf(siteSummaries);
    }

    @Override
    public List<SiteSummary> getSites(PortalOpsRequestContext context) {
        List<SiteSummary> siteSummaries = _getSiteSummaries(context);

        siteSummaries.sort(
                Comparator.comparing(
                        SiteSummary::getTitle, String.CASE_INSENSITIVE_ORDER));

        return List.copyOf(siteSummaries);
    }

    @Override
    public List<SiteFinding> getOrphanedPages(PortalOpsRequestContext context) {
        return List.of();
    }

    @Override
    public List<SiteFinding> getSiteAnomalies(PortalOpsRequestContext context) {
        List<SiteFinding> siteFindings = new ArrayList<>();

        for (SiteSummary siteSummary : _getSiteSummaries(context)) {
            if ((siteSummary.getPublicPages() + siteSummary.getPrivatePages()) == 0) {
                siteFindings.add(
                        new SiteFinding(
                                "site-without-pages",
                                "This site does not currently expose public or private pages.",
                                siteSummary.getTitle()));
            }

            if (siteSummary.getUserCount() == 0) {
                siteFindings.add(
                        new SiteFinding(
                                "site-without-members",
                                "This site currently has no direct members.",
                                siteSummary.getTitle()));
            }
        }

        return List.copyOf(siteFindings);
    }

    private List<SiteSummary> _getSiteSummaries(
            PortalOpsRequestContext context) {

        List<SiteSummary> siteSummaries = new ArrayList<>();

        for (Group group :
                _groupLocalService.getCompanyGroups(
                        context.getCompanyId(), QueryUtil.ALL_POS,
                        QueryUtil.ALL_POS)) {

            if (!group.isRegularSite() || group.isControlPanel() ||
                group.isStagingGroup()) {

                continue;
            }

            siteSummaries.add(_toSiteSummary(group));
        }

        return siteSummaries;
    }

    private String _toInstant(Date date) {
        if (date == null) {
            return null;
        }

        return Instant.ofEpochMilli(date.getTime()).toString();
    }

    private SiteSummary _toSiteSummary(Group group) {
        return new SiteSummary(
                group.isActive(), group.getFriendlyURL(), group.getGroupId(),
                _toInstant(group.getModifiedDate()),
                group.hasPrivateLayouts() ? group.getPrivateLayoutsPageCount() : 0,
                group.hasPublicLayouts() ? group.getPublicLayoutsPageCount() : 0,
                group.getNameCurrentValue(),
                _userLocalService.getGroupUsersCount(group.getGroupId()));
    }

    @Reference
    private GroupLocalService _groupLocalService;

    @Reference
    private UserLocalService _userLocalService;

}
