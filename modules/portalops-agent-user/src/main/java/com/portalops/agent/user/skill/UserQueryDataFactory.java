package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.UserData;
import com.portalops.agent.user.dto.UserQueryData;
import com.portalops.agent.user.dto.UsersData;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserQueryDataFactory {

	public static UserQueryData createAll(
		String queryType, UsersData usersData) {

		return _create(queryType, usersData, userData -> true);
	}

	public static UserQueryData createFiltered(
		String queryType, UsersData usersData, Predicate<UserData> predicate) {

		return _create(queryType, usersData, predicate);
	}

	private static UserQueryData _create(
		String queryType, UsersData usersData, Predicate<UserData> predicate) {

		List<UserData> matchingUsers = usersData.getUsers(
		).stream(
		).filter(
			predicate
		).collect(
			Collectors.toList()
		);

		return new UserQueryData(
			usersData.getActiveUsersCount(),
			usersData.getAdministratorAccountsCount(),
			usersData.getCompanyId(), usersData.getInactiveUsersCount(),
			usersData.getLockedUsersCount(), queryType,
			usersData.getTotalUsers(), matchingUsers);
	}

	private UserQueryDataFactory() {
	}

}
