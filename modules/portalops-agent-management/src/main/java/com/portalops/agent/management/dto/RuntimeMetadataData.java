package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RuntimeMetadataData implements Serializable {

	public RuntimeMetadataData(
		List<AgentMetadataData> agents,
		List<CapabilityMetadataData> capabilities,
		List<DomainMetadataData> domains,
		List<SkillMetadataData> skills,
		List<ToolMetadataData> tools) {

		_agents = List.copyOf(Objects.requireNonNull(agents));
		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_domains = List.copyOf(Objects.requireNonNull(domains));
		_skills = List.copyOf(Objects.requireNonNull(skills));
		_tools = List.copyOf(Objects.requireNonNull(tools));
	}

	public List<AgentMetadataData> getAgents() {
		return _agents;
	}

	public List<CapabilityMetadataData> getCapabilities() {
		return _capabilities;
	}

	public List<DomainMetadataData> getDomains() {
		return _domains;
	}

	public List<SkillMetadataData> getSkills() {
		return _skills;
	}

	public List<ToolMetadataData> getTools() {
		return _tools;
	}

	private final List<AgentMetadataData> _agents;
	private final List<CapabilityMetadataData> _capabilities;
	private final List<DomainMetadataData> _domains;
	private final List<SkillMetadataData> _skills;
	private final List<ToolMetadataData> _tools;

}
