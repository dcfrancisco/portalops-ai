package com.portalops.agent.user.tool;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.portalops.agent.user.dto.UserData;
import com.portalops.agent.user.dto.UsersData;
import com.portalops.api.runtime.PortalOpsTool;

import java.time.Instant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {GetUsersTool.class, PortalOpsTool.class})
public class GetUsersTool implements PortalOpsTool {

	public UsersData execute() {
		long companyId = CompanyThreadLocal.getCompanyId();

		_checkPermission(companyId);

		List<UserData> userDataList = new ArrayList<>();

		for (User user :
				_userLocalService.getCompanyUsers(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

			userDataList.add(_toUserData(user));
		}

		userDataList.sort(
			Comparator.comparing(
				UserData::getFullName, String.CASE_INSENSITIVE_ORDER));

		if (_log.isInfoEnabled()) {
			_log.info(
				"Resolved " + userDataList.size() +
					" portal users for companyId " + companyId);
		}

		return new UsersData(companyId, userDataList);
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Company-scoped user retrieval",
			"User role, organization, and user group membership retrieval");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured users and memberships from the current " +
			"Liferay company.";
	}

	@Override
	public String getName() {
		return "GetUsersTool";
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

	private List<String> _getOrganizations(User user) {
		try {
			return user.getOrganizations(
			).stream(
			).map(
				Organization::getName
			).sorted(
				String.CASE_INSENSITIVE_ORDER
			).toList();
		}
		catch (PortalException portalException) {
			_log.warn(
				"Unable to resolve organizations for user " + user.getUserId(),
				portalException);

			return List.of();
		}
	}

	private List<String> _getRoles(User user) {
		return user.getRoles(
		).stream(
		).map(
			Role::getName
		).sorted(
			String.CASE_INSENSITIVE_ORDER
		).toList();
	}

	private List<String> _getUserGroups(User user) {
		return user.getUserGroups(
		).stream(
		).map(
			UserGroup::getName
		).sorted(
			String.CASE_INSENSITIVE_ORDER
		).toList();
	}

	private String _toInstant(Date date) {
		if (date == null) {
			return null;
		}

		Instant instant = date.toInstant();

		return instant.toString();
	}

	private UserData _toUserData(User user) {
		return new UserData(
			_toInstant(user.getCreateDate()), user.getEmailAddress(),
			user.getFullName(), _toInstant(user.getLastLoginDate()),
			_getOrganizations(user), _getRoles(user),
			WorkflowConstants.getStatusLabel(user.getStatus()),
			user.getUserId(), _getUserGroups(user));
	}

	private static final Log _log = LogFactoryUtil.getLog(GetUsersTool.class);

	@Reference
	private UserLocalService _userLocalService;

}
