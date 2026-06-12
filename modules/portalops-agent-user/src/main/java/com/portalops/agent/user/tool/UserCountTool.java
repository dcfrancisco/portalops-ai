package com.portalops.agent.user.tool;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;

import com.portalops.api.runtime.PortalOpsTool;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsTool.class, UserCountTool.class})
public class UserCountTool implements PortalOpsTool {

	public int execute() {
		long companyId = CompanyThreadLocal.getCompanyId();
		int userCount = _userLocalService.getCompanyUsersCount(companyId);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Resolved portal user count for companyId " + companyId +
					": " + userCount);
		}

		return userCount;
	}

	@Override
	public List<String> getCapabilities() {
		return List.of("User count retrieval for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Counts users in the current portal instance using company-scoped Liferay services.";
	}

	@Override
	public String getName() {
		return "UserCountTool";
	}

	private static final Log _log = LogFactoryUtil.getLog(UserCountTool.class);

	@Reference
	private UserLocalService _userLocalService;

}
