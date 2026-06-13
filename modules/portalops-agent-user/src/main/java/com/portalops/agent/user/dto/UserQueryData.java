package com.portalops.agent.user.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserQueryData implements Serializable {

	public static final String TYPE_ACTIVE_USERS = "active-users";
	public static final String TYPE_ADMINISTRATORS = "administrators";
	public static final String TYPE_INACTIVE_USERS = "inactive-users";
	public static final String TYPE_LOCKED_USERS = "locked-users";
	public static final String TYPE_USERS = "users";

	public UserQueryData(
		int activeUsers, int administratorAccounts, long companyId,
		int inactiveUsers, int lockedUsers, String queryType,
		int totalUsers, List<UserData> users) {

		_activeUsers = activeUsers;
		_administratorAccounts = administratorAccounts;
		_companyId = companyId;
		_inactiveUsers = inactiveUsers;
		_lockedUsers = lockedUsers;
		_queryType = Objects.requireNonNull(queryType);
		_totalUsers = totalUsers;
		_users = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(users)));
	}

	public int getActiveUsers() {
		return _activeUsers;
	}

	public int getAdministratorAccounts() {
		return _administratorAccounts;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public int getInactiveUsers() {
		return _inactiveUsers;
	}

	public int getLockedUsers() {
		return _lockedUsers;
	}

	public int getMatchedUsers() {
		return _users.size();
	}

	public String getQueryType() {
		return _queryType;
	}

	public int getTotalUsers() {
		return _totalUsers;
	}

	public List<UserData> getUsers() {
		return _users;
	}

	private final int _activeUsers;
	private final int _administratorAccounts;
	private final long _companyId;
	private final int _inactiveUsers;
	private final int _lockedUsers;
	private final String _queryType;
	private final int _totalUsers;
	private final List<UserData> _users;

}
