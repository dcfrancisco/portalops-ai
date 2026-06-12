package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.tool.UserCountTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetUserCountSkill implements Skill {

	public static final String NAME = "GetUserCount";

	@Override
	public Object execute() {
		int userCount = _userCountTool.execute();
		String noun = userCount == 1 ? "user" : "users";
		String verb = userCount == 1 ? "is" : "are";

		return new AgentResponse(
			true, "There " + verb + " " + userCount + " " + noun +
				" in the portal.",
			List.of("count=" + userCount),
			List.of(getName(), _userCountTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of("User count retrieval for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves the current portal instance user count.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"How many users are in the portal?",
			"What is the user count?",
			"Count all users.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_userCountTool.getName());
	}

	@Reference
	private UserCountTool _userCountTool;

}
