package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.dto.UserData;
import com.portalops.agent.user.dto.UserQueryData;
import com.portalops.agent.user.dto.UsersData;
import com.portalops.agent.user.tool.GetUsersTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetActiveUsersSkill implements Skill {

	public static final String NAME = "GetActiveUsers";

	@Override
	public Object execute() {
		UsersData usersData = _getUsersTool.execute();

		return AgentResponse.success(
			UserQueryDataFactory.createFiltered(
				UserQueryData.TYPE_ACTIVE_USERS, usersData,
				UserData::isActive),
			List.of(getName(), _getUsersTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve active users in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves active users in the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"How many active users do we have?",
			"List active users in this portal.",
			"Tell me about active users.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_getUsersTool.getName());
	}

	@Reference
	private GetUsersTool _getUsersTool;

}
