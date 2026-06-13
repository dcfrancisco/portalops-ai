package com.portalops.agent.user.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.user.dto.AgentRequest;
import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.agent.user.skill.GetActiveUsersSkill;
import com.portalops.agent.user.skill.GetAdministratorsSkill;
import com.portalops.agent.user.skill.GetInactiveUsersSkill;
import com.portalops.agent.user.skill.GetLockedUsersSkill;
import com.portalops.agent.user.skill.GetUsersSkill;
import com.portalops.agent.user.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

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

		String skillName = _resolveSkillName(agentRequest.getPrompt());
		Skill skill = _getSkill(skillName);

		if (skill == null) {
			_log.error(
				"Unable to execute user request because " + skillName +
					" is unavailable");

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
			"Retrieve and analyze users in the current portal instance",
			"Retrieve active users in the current portal instance",
			"Retrieve inactive users in the current portal instance",
			"Retrieve locked users in the current portal instance",
			"Retrieve administrator accounts in the current portal " +
				"instance");
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
		return List.of(
			GetUsersSkill.NAME, GetActiveUsersSkill.NAME,
			GetInactiveUsersSkill.NAME, GetLockedUsersSkill.NAME,
			GetAdministratorsSkill.NAME);
	}

	private Skill _getSkill(String name) {
		for (Skill skill : _skills) {
			if (name.equals(skill.getName())) {
				return skill;
			}
		}

		return null;
	}

	private String _resolveSkillName(String prompt) {
		String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

		if (_containsAny(
				normalizedPrompt, "administrator", "administrators",
				"admin account", "admin accounts", "admin user",
				"admin users")) {

			return GetAdministratorsSkill.NAME;
		}

		if (_containsAny(normalizedPrompt, "locked", "lockout")) {
			return GetLockedUsersSkill.NAME;
		}

		if (normalizedPrompt.contains("inactive")) {
			return GetInactiveUsersSkill.NAME;
		}

		if (_containsAny(normalizedPrompt, "active user", "active users",
				"approved user", "approved users")) {

			return GetActiveUsersSkill.NAME;
		}

		return GetUsersSkill.NAME;
	}

	private boolean _containsAny(String value, String... candidates) {
		for (String candidate : candidates) {
			if (value.contains(candidate)) {
				return true;
			}
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
