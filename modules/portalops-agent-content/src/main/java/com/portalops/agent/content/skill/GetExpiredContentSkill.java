package com.portalops.agent.content.skill;

import com.portalops.agent.content.dto.AgentResponse;
import com.portalops.agent.content.dto.ContentData;
import com.portalops.agent.content.tool.GetContentTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class GetExpiredContentSkill implements Skill {

	public static final String NAME = "GetExpiredContent";

	@Override
	public Object execute() {
		ContentData contentData = _getContentTool.execute();

		return AgentResponse.success(
			ContentQueryDataFactory.createExpired(contentData),
			List.of(getName(), _getContentTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"Retrieve expired content in the current portal instance");
	}

	@Override
	public String getDescription() {
		return "Retrieves expired content for the current portal instance.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Show expired content.",
			"How much expired content do we have?",
			"Tell me about expired content.");
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
