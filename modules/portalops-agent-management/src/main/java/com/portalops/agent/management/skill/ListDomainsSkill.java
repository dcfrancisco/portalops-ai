package com.portalops.agent.management.skill;

import com.portalops.agent.management.dto.AgentResponse;
import com.portalops.agent.management.dto.RuntimeMetadataData;
import com.portalops.agent.management.tool.GetRuntimeMetadataTool;
import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {PortalOpsSkill.class, Skill.class})
public class ListDomainsSkill implements Skill {

	public static final String NAME = "ListDomains";

	@Override
	public Object execute(String prompt) {
		RuntimeMetadataData runtimeMetadataData =
			_getRuntimeMetadataTool.execute();

		return AgentResponse.success(
			ManagementQueryDataFactory.createListDomains(
				runtimeMetadataData),
			List.of(getName(), _getRuntimeMetadataTool.getName()));
	}

	@Override
	public List<String> getCapabilities() {
		return List.of("List PortalOps domains");
	}

	@Override
	public String getDescription() {
		return "Lists the PortalOps operational domains currently available.";
	}

	@Override
	public List<String> getExamplePrompts() {
		return List.of(
			"List domains",
			"What domains do you support?",
			"Show PortalOps domains");
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
