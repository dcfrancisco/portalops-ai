package com.portalops.agent.management.skill;

import com.portalops.agent.management.dto.AgentResponse;
import com.portalops.agent.management.dto.RuntimeMetadataData;
import com.portalops.agent.management.tool.GetRuntimeMetadataTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class DescribeCapabilitySkill implements Skill {

	public static final String NAME = "DescribeCapability";

	@Override
	public Object execute(String prompt) {
		RuntimeMetadataData runtimeMetadataData =
			_getRuntimeMetadataTool.execute();

		return AgentResponse.success(
			ManagementQueryDataFactory.createDescribe(
				prompt, runtimeMetadataData),
			List.of(getName(), _getRuntimeMetadataTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of("Describe a PortalOps capability or domain");
	}

	@Override
	public String getDescription() {
		return "Describes a PortalOps domain or capability using runtime metadata.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"Describe User Management",
			"Describe Search",
			"Describe Sites");
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
