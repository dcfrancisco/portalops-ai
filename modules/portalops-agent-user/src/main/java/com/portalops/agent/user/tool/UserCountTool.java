package com.portalops.agent.user.tool;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = UserCountTool.class)
public class UserCountTool {

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

	private static final Log _log = LogFactoryUtil.getLog(UserCountTool.class);

	@Reference
	private UserLocalService _userLocalService;

}
