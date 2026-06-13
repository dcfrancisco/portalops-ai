package com.portalops.agent.user.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.user.dto.AgentRequest;
import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.skill.GetUsersSkill;
import com.portalops.agent.user.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {PortalOpsAgent.class, UserAgent.class})
public class UserManagementAgent implements UserAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (agentRequest.getPrompt(
			).isBlank()) {

			return AgentResponse.failure("EMPTY_USER_REQUEST");
		}

		Skill skill = _getSkill(GetUsersSkill.NAME);

		if (skill == null) {
			_log.error(
				"Unable to execute user request because GetUsers is " +
					"unavailable");

			return AgentResponse.failure("USER_SKILL_UNAVAILABLE");
		}

		Object result;

		try {
			result = skill.execute();
		}
		catch (SecurityException securityException) {
			_log.warn("User data access was denied", securityException);

			return AgentResponse.failure("USER_ACCESS_DENIED");
		}
		catch (RuntimeException runtimeException) {
			_log.error("Unable to collect user data", runtimeException);

			return AgentResponse.failure("USER_DATA_COLLECTION_FAILED");
		}

		if (result instanceof AgentResponse) {
			return (AgentResponse)result;
		}

		_log.error(
			"Skill " + skill.getName() +
				" returned an unsupported response type");

		return AgentResponse.failure("INVALID_SKILL_RESPONSE");
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve and analyze users in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "User administration operations.";
	}

	@Override
	public String getName() {
		return "UserManagementAgent";
	}

	@Override
	public List<String> getSupportedSkills() {
		return List.of(GetUsersSkill.NAME);
	}

	private Skill _getSkill(String name) {
		for (Skill skill : _skills) {
			if (name.equals(skill.getName())) {
				return skill;
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
