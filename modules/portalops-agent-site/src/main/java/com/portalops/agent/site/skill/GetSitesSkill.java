package com.portalops.agent.site.skill;

import com.portalops.agent.site.dto.AgentResponse;
import com.portalops.agent.site.dto.SitesData;
import com.portalops.agent.site.tool.GetSitesTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetSitesSkill implements Skill {

	public static final String NAME = "GetSites";

	@Override
	public Object execute(String prompt) {
		SitesData sitesData = _getSitesTool.execute();

		return AgentResponse.success(
			SiteQueryDataFactory.create(prompt, sitesData),
			List.of(getName(), _getSitesTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve sites in the current portal instance",
			"Retrieve site and page summaries in the current portal instance",
			"Retrieve public and private page names grouped by site");
	}

	@Override
	public String getDescription() {
		return "Retrieves structured site and page data for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"List sites in this portal.",
			"How many sites do we have?",
			"List sites and page names.",
			"What pages are in Guest?");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_getSitesTool.getName());
	}

	@Reference
	private GetSitesTool _getSitesTool;

}
