package com.portalops.agent.site.tool;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import com.portalops.agent.site.dto.PageData;
import com.portalops.agent.site.dto.SiteData;
import com.portalops.agent.site.dto.SitesData;
import com.portalops.api.runtime.PortalOpsTool;

import java.time.Instant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {GetSitesTool.class, PortalOpsTool.class})
public class GetSitesTool implements PortalOpsTool {

	public SitesData execute() {
		long companyId = CompanyThreadLocal.getCompanyId();

		_checkPermission(companyId);

		List<SiteData> sites = new ArrayList<>();

		for (Group group :
				_groupLocalService.getCompanyGroups(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

			if (!group.isRegularSite() || group.isControlPanel() ||
				group.isStagingGroup()) {

				continue;
			}

			sites.add(_toSiteData(group));
		}

		sites.sort(
			Comparator.comparing(
				SiteData::getName, String.CASE_INSENSITIVE_ORDER));

		return new SitesData(companyId, sites);
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Company-scoped site retrieval",
			"Public and private page inventory by site");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured sites and grouped public/private pages from the current Liferay company.";
	}

	@Override
	public String getName() {
		return "GetSitesTool";
	}

	private void _checkPermission(long companyId) {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if ((permissionChecker == null) ||
			(!permissionChecker.isOmniadmin() &&
			 !permissionChecker.isCompanyAdmin(companyId))) {

			throw new SecurityException(
				"Company administrator permission is required");
		}
	}

	private List<PageData> _getPages(long groupId, boolean privateLayout) {
		List<PageData> pageDataList = new ArrayList<>();

		for (Layout layout : _layoutLocalService.getLayouts(groupId, privateLayout)) {
			pageDataList.add(
				new PageData(
					layout.getFriendlyURL(), layout.isHidden(),
					layout.getLayoutId(), layout.getNameCurrentValue(),
					(layout.getParentLayoutId() > 0) ?
						layout.getParentLayoutId() : null,
					layout.isPrivateLayout(), layout.getPlid(), layout.getType()));
		}

		pageDataList.sort(
			Comparator.comparing(
				PageData::getName, String.CASE_INSENSITIVE_ORDER));

		return List.copyOf(pageDataList);
	}

	private String _toInstant(Date date) {
		if (date == null) {
			return null;
		}

		return Instant.ofEpochMilli(date.getTime()).toString();
	}

	private SiteData _toSiteData(Group group) {
		List<PageData> privatePages = _getPages(group.getGroupId(), true);
		List<PageData> publicPages = _getPages(group.getGroupId(), false);

		return new SiteData(
			group.isActive(), group.getCompanyId(), group.getFriendlyURL(),
			group.getGroupId(), _toInstant(group.getModifiedDate()),
			group.getNameCurrentValue(), privatePages.size(), privatePages,
			publicPages.size(), publicPages, group.isSite(),
			_userLocalService.getGroupUsersCount(group.getGroupId()));
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private UserLocalService _userLocalService;

}
