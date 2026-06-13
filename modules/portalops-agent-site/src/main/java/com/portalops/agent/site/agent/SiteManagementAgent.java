package com.portalops.agent.site.agent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.portalops.agent.site.dto.AgentRequest;
import com.portalops.agent.site.dto.AgentResponse;
import com.portalops.agent.site.skill.GetSitesSkill;
import com.portalops.agent.site.skill.Skill;
import com.portalops.api.runtime.PortalOpsAgent;

import java.util.Collection;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {PortalOpsAgent.class, SiteAgent.class})
public class SiteManagementAgent implements PortalOpsAgent, SiteAgent {

	@Override
	public AgentResponse execute(String prompt) {
		AgentRequest agentRequest = new AgentRequest(prompt);

		if (agentRequest.getPrompt().isBlank()) {
			return AgentResponse.failure("EMPTY_SITE_REQUEST");
		}

		Skill skill = _getSkill(GetSitesSkill.NAME);

		if (skill == null) {
			_log.error("Unable to execute site request because GetSites is unavailable");

			return AgentResponse.failure("SITE_SKILL_UNAVAILABLE");
		}

		try {
			Object result = skill.execute(agentRequest.getPrompt());

			if (result instanceof AgentResponse) {
				return (AgentResponse)result;
			}
		}
		catch (SecurityException securityException) {
			_log.warn("Site data access was denied", securityException);

			return AgentResponse.failure("SITE_ACCESS_DENIED");
		}
		catch (RuntimeException runtimeException) {
			_log.error("Unable to collect site data", runtimeException);

			return AgentResponse.failure("SITE_DATA_COLLECTION_FAILED");
		}

		_log.error("GetSites returned an unsupported response type");

		return AgentResponse.failure("INVALID_SITE_SKILL_RESPONSE");
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve sites in the current portal instance",
			"Retrieve site and page summaries in the current portal instance",
			"Retrieve public pages grouped by site",
			"Retrieve private pages grouped by site");
	}

	@Override
	public String getDescription() {
		return "Site administration operations.";
	}

	@Override
	public String getName() {
		return "SiteManagementAgent";
	}

	@Override
	public List<String> getSupportedSkills() {
		return List.of(GetSitesSkill.NAME);
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
		SiteManagementAgent.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<Skill> _skills;

}
