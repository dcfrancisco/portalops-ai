package com.portalops.agent.user.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.user.dto.AgentRequest;
import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.skill.GetUserCountSkill;
import com.portalops.agent.user.skill.Skill;

import java.util.Collection;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = UserAgent.class)
public class UserManagementAgent implements UserAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (_isGetUserCountRequest(agentRequest)) {
			Skill skill = _getSkill(GetUserCountSkill.NAME);

			if (skill == null) {
				_log.error(
					"Unable to execute user count request because the skill is unavailable");

				return new AgentResponse(
					false,
					"PortalOps could not load the Get User Count skill.");
			}

			Object result = skill.execute();

			if (result instanceof AgentResponse) {
				return (AgentResponse)result;
			}

			_log.error(
				"Skill " + skill.getName() +
					" returned an unsupported response type");

			return new AgentResponse(
				false,
				"PortalOps received an invalid response from the Get User Count skill.");
		}

		return new AgentResponse(
			false,
			"User Management Agent currently supports only user count requests.");
	}

	private Skill _getSkill(String name) {
		for (Skill skill : _skills) {
			if (name.equals(skill.getName())) {
				return skill;
			}
		}

		return null;
	}

	private boolean _isGetUserCountRequest(AgentRequest agentRequest) {
		String normalizedPrompt = agentRequest.getPrompt().toLowerCase(
			Locale.ROOT);

		return normalizedPrompt.contains("how many users") ||
			normalizedPrompt.contains("user count") ||
			(normalizedPrompt.contains("count") &&
			 normalizedPrompt.contains("user"));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserManagementAgent.class);

	@Reference
	private Collection<Skill> _skills;

}
