package com.portalops.agent.search.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.search.dto.AgentRequest;
import com.portalops.agent.search.dto.AgentResponse;
import com.portalops.agent.search.skill.GetReindexStatusSkill;
import com.portalops.agent.search.skill.GetSearchErrorsSkill;
import com.portalops.agent.search.skill.GetSearchHealthSkill;
import com.portalops.agent.search.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {PortalOpsAgent.class, SearchAgent.class})
public class SearchManagementAgent implements PortalOpsAgent, SearchAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (agentRequest.getPrompt().isBlank()) {
			return AgentResponse.failure("EMPTY_SEARCH_REQUEST");
		}

		String skillName = _resolveSkillName(agentRequest.getPrompt());
		Skill skill = _getSkill(skillName);

		if (skill == null) {
			_log.error(
				"Unable to execute search request because " + skillName +
					" is unavailable");

			return AgentResponse.failure("SEARCH_SKILL_UNAVAILABLE");
		}

		try {
			Object result = skill.execute();

			if (result instanceof AgentResponse) {
				return (AgentResponse)result;
			}
		}
		catch (SecurityException securityException) {
			_log.warn("Search data access was denied", securityException);

			return AgentResponse.failure("SEARCH_ACCESS_DENIED");
		}
		catch (RuntimeException runtimeException) {
			_log.error("Unable to collect search data", runtimeException);

			return AgentResponse.failure("SEARCH_DATA_COLLECTION_FAILED");
		}

		_log.error(
			"Skill " + skill.getName() +
				" returned an unsupported response type");

		return AgentResponse.failure("INVALID_SEARCH_SKILL_RESPONSE");
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve search health for the current portal instance",
			"Retrieve reindex status for the current portal instance",
			"Retrieve search diagnostics for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Search administration operations.";
	}

	@Override
	public String getName() {
		return "SearchManagementAgent";
	}

	@Override
	public List<String> getSupportedSkills() {
		return List.of(
			GetSearchHealthSkill.NAME, GetReindexStatusSkill.NAME,
			GetSearchErrorsSkill.NAME);
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

		if (normalizedPrompt.contains("reindex")) {
			return GetReindexStatusSkill.NAME;
		}

		if (normalizedPrompt.contains("error") ||
			normalizedPrompt.contains("issue") ||
			normalizedPrompt.contains("failing") ||
			normalizedPrompt.contains("not working")) {

			return GetSearchErrorsSkill.NAME;
		}

		return GetSearchHealthSkill.NAME;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
