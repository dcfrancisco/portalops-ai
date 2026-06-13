package com.portalops.assistant.api.payload;

public class UserFindingsPayload implements AssistantPayload {

	public UserFindingsPayload(
		int activeUsers, int administratorAccounts, int inactiveUsers,
		int lockedUsers, int matchingUsers, String queryType, int totalUsers) {

		_activeUsers = activeUsers;
		_administratorAccounts = administratorAccounts;
		_inactiveUsers = inactiveUsers;
		_lockedUsers = lockedUsers;
		_matchingUsers = matchingUsers;
		_queryType = queryType;
		_totalUsers = totalUsers;
	}

	public int getActiveUsers() {
		return _activeUsers;
	}

	public int getAdministratorAccounts() {
		return _administratorAccounts;
	}

	public int getInactiveUsers() {
		return _inactiveUsers;
	}

	public int getLockedUsers() {
		return _lockedUsers;
	}

	public int getMatchingUsers() {
		return _matchingUsers;
	}

	public String getQueryType() {
		return _queryType;
	}

	public int getTotalUsers() {
		return _totalUsers;
	}

	private final int _activeUsers;
	private final int _administratorAccounts;
	private final int _inactiveUsers;
	private final int _lockedUsers;
	private final int _matchingUsers;
	private final String _queryType;
	private final int _totalUsers;

}
