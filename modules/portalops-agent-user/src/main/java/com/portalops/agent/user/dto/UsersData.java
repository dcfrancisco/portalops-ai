package com.portalops.agent.user.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UsersData implements Serializable {

	public UsersData(long companyId, List<UserData> users) {
		_companyId = companyId;
		_users = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(users)));
	}

	public long getCompanyId() {
		return _companyId;
	}

	public int getTotalUsers() {
		return _users.size();
	}

	public List<UserData> getUsers() {
		return _users;
	}

	public int getActiveUsersCount() {
		return (int)_users.stream(
		).filter(
			UserData::isActive
		).count();
	}

	public int getAdministratorAccountsCount() {
		return (int)_users.stream(
		).filter(
			UserData::isAdministrator
		).count();
	}

	public int getInactiveUsersCount() {
		return (int)_users.stream(
		).filter(
			userData -> !userData.isActive()
		).count();
	}

	public int getLockedUsersCount() {
		return (int)_users.stream(
		).filter(
			UserData::isLocked
		).count();
	}

	private final long _companyId;
	private final List<UserData> _users;

}
