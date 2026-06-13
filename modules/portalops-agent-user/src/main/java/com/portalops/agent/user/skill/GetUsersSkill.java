package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.dto.UsersData;
import com.portalops.agent.user.tool.GetUsersTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetUsersSkill implements Skill {

	public static final String NAME = "GetUsers";

	@Override
	public Object execute() {
		UsersData usersData = _getUsersTool.execute();

		return AgentResponse.success(
			usersData, List.of(getName(), _getUsersTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve and analyze users in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured user and membership data for the " +
			"current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Tell me about the users in this portal.",
			"How many users do we have?",
			"What do you notice about the users?");
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
