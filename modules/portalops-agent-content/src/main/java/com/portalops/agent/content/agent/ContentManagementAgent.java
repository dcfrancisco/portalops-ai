package com.portalops.agent.content.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.content.dto.AgentRequest;
import com.portalops.agent.content.dto.AgentResponse;
import com.portalops.agent.content.skill.GetContentSummarySkill;
import com.portalops.agent.content.skill.GetExpiredContentSkill;
import com.portalops.agent.content.skill.GetPendingContentSkill;
import com.portalops.agent.content.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {ContentAgent.class, PortalOpsAgent.class})
public class ContentManagementAgent implements ContentAgent, PortalOpsAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (agentRequest.getPrompt().isBlank()) {
			return AgentResponse.failure("EMPTY_CONTENT_REQUEST");
		}

		String skillName = _resolveSkillName(agentRequest.getPrompt());
		Skill skill = _getSkill(skillName);

		if (skill == null) {
			_log.error(
				"Unable to execute content request because " + skillName +
					" is unavailable");

			return AgentResponse.failure("CONTENT_SKILL_UNAVAILABLE");
		}

		try {
			Object result = skill.execute();

			if (result instanceof AgentResponse) {
				return (AgentResponse)result;
			}
		}
		catch (SecurityException securityException) {
			_log.warn("Content data access was denied", securityException);

			return AgentResponse.failure("CONTENT_ACCESS_DENIED");
		}
		catch (RuntimeException runtimeException) {
			_log.error("Unable to collect content data", runtimeException);

			return AgentResponse.failure("CONTENT_DATA_COLLECTION_FAILED");
		}

		_log.error(
			"Skill " + skill.getName() +
				" returned an unsupported response type");

		return AgentResponse.failure("INVALID_CONTENT_SKILL_RESPONSE");
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve content summaries in the current portal instance",
			"Retrieve expired content in the current portal instance",
			"Retrieve pending content in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Content administration operations.";
	}

	@Override
	public String getName() {
		return "ContentManagementAgent";
	}

	@Override
	public List<String> getSupportedSkills() {
		return List.of(
			GetContentSummarySkill.NAME, GetExpiredContentSkill.NAME,
			GetPendingContentSkill.NAME);
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

		if (normalizedPrompt.contains("expired")) {
			return GetExpiredContentSkill.NAME;
		}

		if (normalizedPrompt.contains("pending") ||
			normalizedPrompt.contains("draft")) {

			return GetPendingContentSkill.NAME;
		}

		return GetContentSummarySkill.NAME;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContentManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
