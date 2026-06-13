package com.portalops.agent.management.skill;

import com.portalops.agent.management.dto.AgentResponse;
import com.portalops.agent.management.dto.RuntimeMetadataData;
import com.portalops.agent.management.tool.GetRuntimeMetadataTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class ListAgentsSkill implements Skill {

	public static final String NAME = "ListAgents";

	@Override
	public Object execute(String prompt) {
		RuntimeMetadataData runtimeMetadataData =
			_getRuntimeMetadataTool.execute();

		return AgentResponse.success(
			ManagementQueryDataFactory.createListAgents(
				runtimeMetadataData),
			List.of(getName(), _getRuntimeMetadataTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of("List PortalOps agents");
	}

	@Override
	public String getDescription() {
		return "Lists the currently registered PortalOps agents.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"List agents",
			"Show PortalOps agents",
			"What agents are registered?");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<String> getSupportedTools() {
		return List.of(_getRuntimeMetadataTool.getName());
	}

	@Reference
	private GetRuntimeMetadataTool _getRuntimeMetadataTool;

}
