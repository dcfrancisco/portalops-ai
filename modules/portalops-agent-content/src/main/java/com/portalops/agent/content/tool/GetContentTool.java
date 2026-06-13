package com.portalops.agent.content.tool;

import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;

import com.portalops.agent.content.dto.ContentData;
import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.runtime.PortalOpsTool;
import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {GetContentTool.class, PortalOpsTool.class})
public class GetContentTool implements PortalOpsTool {

	public ContentData execute() {
		long companyId = CompanyThreadLocal.getCompanyId();

		_checkPermission(companyId);

		PortalOpsRequestContext portalOpsRequestContext =
			new PortalOpsRequestContext(companyId, 0, 0);

		return new ContentData(
			companyId,
			_contentInspectionService.getContentSummary(portalOpsRequestContext),
			_contentInspectionService.getExpiredContent(portalOpsRequestContext),
			_contentInspectionService.getPendingContent(portalOpsRequestContext));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Company-scoped web content retrieval",
			"Expired content retrieval",
			"Pending content retrieval");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured web content summaries from the current Liferay company.";
	}

	@Override
	public String getName() {
		return "GetContentTool";
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
	private ContentInspectionService _contentInspectionService;

}
