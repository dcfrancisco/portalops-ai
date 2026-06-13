package com.portalops.assistant.api.payload;

public class UserFindingsPayload implements AssistantPayload {

	public UserFindingsPayload(
		int activeUsers, int administratorAccounts, int totalUsers) {

		_activeUsers = activeUsers;
		_administratorAccounts = administratorAccounts;
		_totalUsers = totalUsers;
	}

	public int getActiveUsers() {
		return _activeUsers;
	}

	public int getAdministratorAccounts() {
		return _administratorAccounts;
	}

	public int getTotalUsers() {
		return _totalUsers;
	}

	private final int _activeUsers;
	private final int _administratorAccounts;
	private final int _totalUsers;

}
