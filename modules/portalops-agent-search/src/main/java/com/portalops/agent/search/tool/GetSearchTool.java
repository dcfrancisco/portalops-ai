package com.portalops.agent.search.tool;

import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;

import com.portalops.agent.search.dto.SearchData;
import com.portalops.api.runtime.PortalOpsTool;
import com.portalops.api.search.SearchInspectionService;
import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {GetSearchTool.class, PortalOpsTool.class})
public class GetSearchTool implements PortalOpsTool {

	public SearchData execute() {
		long companyId = CompanyThreadLocal.getCompanyId();

		_checkPermission(companyId);

		return new SearchData(
			_searchInspectionService.getSearchHealth(
				new PortalOpsRequestContext(companyId, 0, 0)));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Company-scoped search health retrieval",
			"Company-scoped reindex status retrieval",
			"Company-scoped search diagnostics retrieval");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured search diagnostics from the current Liferay company.";
	}

	@Override
	public String getName() {
		return "GetSearchTool";
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

	@Reference
	private SearchInspectionService _searchInspectionService;

}
