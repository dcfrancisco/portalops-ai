package com.portalops.agent.user.skill;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.tool.UserCountTool;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Skill.class)
public class GetUserCountSkill implements Skill {

	public static final String NAME = "get-user-count";

	@Override
	public Object execute() {
		int userCount = _userCountTool.execute();

		return new AgentResponse(
			true, "There are " + userCount + " users in the portal.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Reference
	private UserCountTool _userCountTool;

}
