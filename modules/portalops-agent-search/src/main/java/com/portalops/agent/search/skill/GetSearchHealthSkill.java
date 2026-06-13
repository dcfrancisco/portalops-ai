package com.portalops.agent.search.skill;

import com.portalops.agent.search.dto.AgentResponse;
import com.portalops.agent.search.dto.SearchData;
import com.portalops.agent.search.tool.GetSearchTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetSearchHealthSkill implements Skill {

	public static final String NAME = "GetSearchHealth";

	@Override
	public Object execute() {
		SearchData searchData = _getSearchTool.execute();

		return AgentResponse.success(
			SearchQueryDataFactory.createHealth(searchData),
			List.of(getName(), _getSearchTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve search health for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves search health diagnostics for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"How is search doing?",
			"Search health",
			"Is search healthy?");
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
