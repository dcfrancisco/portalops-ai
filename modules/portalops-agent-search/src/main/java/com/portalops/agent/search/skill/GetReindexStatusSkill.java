package com.portalops.agent.search.skill;

import com.portalops.agent.search.dto.AgentResponse;
import com.portalops.agent.search.dto.SearchData;
import com.portalops.agent.search.tool.GetSearchTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetReindexStatusSkill implements Skill {

	public static final String NAME = "GetReindexStatus";

	@Override
	public Object execute() {
		SearchData searchData = _getSearchTool.execute();

		return AgentResponse.success(
			SearchQueryDataFactory.createReindex(searchData),
			List.of(getName(), _getSearchTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve reindex status for the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves reindex status diagnostics for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Do we need to reindex?",
			"Reindex status",
			"Is indexing current?");
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
