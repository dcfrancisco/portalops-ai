package com.portalops.agent.management.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.management.dto.AgentRequest;
import com.portalops.agent.management.dto.AgentResponse;
import com.portalops.agent.management.skill.DescribeCapabilitySkill;
import com.portalops.agent.management.skill.ListAgentsSkill;
import com.portalops.agent.management.skill.ListCapabilitiesSkill;
import com.portalops.agent.management.skill.ListDomainsSkill;
import com.portalops.agent.management.skill.ListSkillsSkill;
import com.portalops.agent.management.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {ManagementAgent.class, PortalOpsAgent.class})
public class PortalOpsManagementAgent implements ManagementAgent, PortalOpsAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (agentRequest.getPrompt().isBlank()) {
			return AgentResponse.failure("EMPTY_MANAGEMENT_REQUEST");
		}

		String skillName = _resolveSkillName(agentRequest.getPrompt());
		Skill skill = _getSkill(skillName);

		if (skill == null) {
			_log.error(
				"Unable to execute management request because " + skillName +
					" is unavailable");

			return AgentResponse.failure("MANAGEMENT_SKILL_UNAVAILABLE");
		}

		try {
			Object result = skill.execute(agentRequest.getPrompt());

			if (result instanceof AgentResponse) {
				return (AgentResponse)result;
			}
		}
		catch (RuntimeException runtimeException) {
			_log.error(
				"Unable to collect PortalOps runtime metadata",
				runtimeException);

			return AgentResponse.failure("MANAGEMENT_DATA_COLLECTION_FAILED");
		}

		_log.error(
			"Skill " + skill.getName() +
				" returned an unsupported response type");

		return AgentResponse.failure("INVALID_MANAGEMENT_SKILL_RESPONSE");
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"List PortalOps agents",
			"List PortalOps skills",
			"List PortalOps domains",
			"List PortalOps capabilities",
			"Describe a PortalOps capability or domain");
	}

	@Override
	public String getDescription() {
		return "PortalOps runtime self-discovery operations.";
	}

	@Override
	public String getName() {
		return "PortalOpsManagementAgent";
	}

	@Override
	public List<String> getSupportedSkills() {
		return List.of(
			ListCapabilitiesSkill.NAME, DescribeCapabilitySkill.NAME,
			ListDomainsSkill.NAME, ListAgentsSkill.NAME, ListSkillsSkill.NAME);
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

		if (normalizedPrompt.contains("list agents")) {
			return ListAgentsSkill.NAME;
		}

		if (normalizedPrompt.contains("list skills")) {
			return ListSkillsSkill.NAME;
		}

		if (normalizedPrompt.contains("list domains")) {
			return ListDomainsSkill.NAME;
		}

		if (normalizedPrompt.contains("describe ")) {
			return DescribeCapabilitySkill.NAME;
		}

		return ListCapabilitiesSkill.NAME;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalOpsManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
