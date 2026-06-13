package com.portalops.agent.search.skill;

import com.portalops.agent.search.dto.AgentResponse;
import com.portalops.agent.search.dto.SearchData;
import com.portalops.agent.search.tool.GetSearchTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetSearchErrorsSkill implements Skill {

	public static final String NAME = "GetSearchErrors";

	@Override
	public Object execute() {
		SearchData searchData = _getSearchTool.execute();

		return AgentResponse.success(
			SearchQueryDataFactory.createErrors(searchData),
			List.of(getName(), _getSearchTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve search diagnostics for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves search errors and warnings for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Search errors",
			"Search issues",
			"Why is search not working?");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_getSearchTool.getName());
	}

	@Reference
	private GetSearchTool _getSearchTool;

}
