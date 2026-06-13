package com.portalops.agent.content.skill;

import com.portalops.agent.content.dto.AgentResponse;
import com.portalops.agent.content.dto.ContentData;
import com.portalops.agent.content.tool.GetContentTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetContentSummarySkill implements Skill {

	public static final String NAME = "GetContentSummary";

	@Override
	public Object execute() {
		ContentData contentData = _getContentTool.execute();

		return AgentResponse.success(
			ContentQueryDataFactory.createAll(contentData),
			List.of(getName(), _getContentTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve content summaries in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves content summaries for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Tell me about the content in this portal.",
			"How much content do we have?",
			"Show content summary.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_getContentTool.getName());
	}

	@Reference
	private GetContentTool _getContentTool;

}
