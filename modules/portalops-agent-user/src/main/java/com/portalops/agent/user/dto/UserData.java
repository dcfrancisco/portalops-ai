package com.portalops.agent.user.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserData implements Serializable {

	public UserData(
		boolean active, boolean administrator, String createDate,
		String emailAddress, String fullName,
		String lastLoginDate, boolean locked, List<String> organizations,
		List<String> roles, String status, long userId,
		List<String> userGroups) {

		_active = active;
		_administrator = administrator;
		_createDate = Objects.requireNonNull(createDate);
		_emailAddress = Objects.requireNonNull(emailAddress);
		_fullName = Objects.requireNonNull(fullName);
		_lastLoginDate = lastLoginDate;
		_locked = locked;
		_organizations = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(organizations)));
		_roles = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(roles)));
		_status = Objects.requireNonNull(status);
		_userId = userId;
		_userGroups = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(userGroups)));
	}

	public String getCreateDate() {
		return _createDate;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public String getFullName() {
		return _fullName;
	}

	public String getLastLoginDate() {
		return _lastLoginDate;
	}

	public List<String> getOrganizations() {
		return _organizations;
	}

	public List<String> getRoles() {
		return _roles;
	}

	public String getStatus() {
		return _status;
	}

	public List<String> getUserGroups() {
		return _userGroups;
	}

	public long getUserId() {
		return _userId;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean isAdministrator() {
		return _administrator;
	}

	public boolean isLocked() {
		return _locked;
	}

	private final boolean _active;
	private final boolean _administrator;
	private final String _createDate;
	private final String _emailAddress;
	private final String _fullName;
	private final String _lastLoginDate;
	private final boolean _locked;
	private final List<String> _organizations;
	private final List<String> _roles;
	private final String _status;
	private final List<String> _userGroups;
	private final long _userId;

}
