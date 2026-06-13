package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.dto.UserQueryData;
import com.portalops.agent.user.dto.UsersData;
import com.portalops.agent.user.tool.GetUsersTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetInactiveUsersSkill implements Skill {

	public static final String NAME = "GetInactiveUsers";

	@Override
	public Object execute() {
		UsersData usersData = _getUsersTool.execute();

		return AgentResponse.success(
			UserQueryDataFactory.createFiltered(
				UserQueryData.TYPE_INACTIVE_USERS, usersData,
				userData -> !userData.isActive()),
			List.of(getName(), _getUsersTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve inactive users in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves inactive users in the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"How many inactive users do we have?",
			"List inactive users in this portal.",
			"Show inactive accounts.");
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
