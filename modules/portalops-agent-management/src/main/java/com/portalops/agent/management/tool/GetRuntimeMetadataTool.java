package com.portalops.agent.management.tool;

import com.portalops.agent.management.dto.AgentMetadataData;
import com.portalops.agent.management.dto.CapabilityMetadataData;
import com.portalops.agent.management.dto.DomainMetadataData;
import com.portalops.agent.management.dto.RuntimeMetadataData;
import com.portalops.agent.management.dto.SkillMetadataData;
import com.portalops.agent.management.dto.ToolMetadataData;
import com.portalops.api.runtime.PortalOpsAgent;
import com.portalops.api.runtime.PortalOpsSkill;
import com.portalops.api.runtime.PortalOpsTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {GetRuntimeMetadataTool.class, PortalOpsTool.class})
public class GetRuntimeMetadataTool implements PortalOpsTool {

	public RuntimeMetadataData execute() {
		List<AgentMetadataData> agents = _getAgents();
		List<SkillMetadataData> skills = _getSkills();
		List<ToolMetadataData> tools = _getTools();

		return new RuntimeMetadataData(
			agents, _getCapabilities(agents, skills, tools),
			_getDomains(agents), skills, tools);
	}

	@Override
	public List<String> getCapabilities() {
		return List.of(
			"PortalOps runtime metadata retrieval",
			"PortalOps capability registry inspection");
	}

	@Override
	public String getDescription() {
		return "Retrieves registered PortalOps domains, agents, skills, tools, and capabilities.";
	}

	@Override
	public String getName() {
		return "GetRuntimeMetadataTool";
	}

	private List<AgentMetadataData> _getAgents() {
		List<AgentMetadataData> agents = new ArrayList<>();

		for (PortalOpsAgent portalOpsAgent : _portalOpsAgents) {
			agents.add(
				new AgentMetadataData(
					portalOpsAgent.getCapabilities(),
					portalOpsAgent.getDescription(),
					_toDomainName(portalOpsAgent.getName()),
					portalOpsAgent.getName(),
					portalOpsAgent.getSupportedSkills()));
		}

		agents.sort(Comparator.comparing(AgentMetadataData::getName));

		return List.copyOf(agents);
	}

	private List<CapabilityMetadataData> _getCapabilities(
		List<AgentMetadataData> agents, List<SkillMetadataData> skills,
		List<ToolMetadataData> tools) {

		Map<String, CapabilityMetadataData> capabilities =
			new LinkedHashMap<>();

		for (AgentMetadataData agentMetadataData : agents) {
			for (String capability : agentMetadataData.getCapabilities()) {
				capabilities.putIfAbsent(
					capability,
					new CapabilityMetadataData(
						agentMetadataData.getDomain(), capability,
						agentMetadataData.getName(), "agent"));
			}
		}

		for (SkillMetadataData skillMetadataData : skills) {
			for (String capability : skillMetadataData.getCapabilities()) {
				capabilities.putIfAbsent(
					capability,
					new CapabilityMetadataData(
						skillMetadataData.getDomain(), capability,
						skillMetadataData.getName(), "skill"));
			}
		}

		for (ToolMetadataData toolMetadataData : tools) {
			for (String capability : toolMetadataData.getCapabilities()) {
				capabilities.putIfAbsent(
					capability,
					new CapabilityMetadataData(
						toolMetadataData.getDomain(), capability,
						toolMetadataData.getName(), "tool"));
			}
		}

		return List.copyOf(capabilities.values());
	}

	private List<DomainMetadataData> _getDomains(
		List<AgentMetadataData> agents) {

		Map<String, DomainMetadataData> domains = new LinkedHashMap<>();

		for (AgentMetadataData agentMetadataData : agents) {
			String domain = agentMetadataData.getDomain();

			domains.putIfAbsent(
				domain,
				new DomainMetadataData(
					agentMetadataData.getDescription(), domain,
					agentMetadataData.getCapabilities()));
		}

		return List.copyOf(domains.values());
	}

	private List<SkillMetadataData> _getSkills() {
		List<SkillMetadataData> skills = new ArrayList<>();

		for (PortalOpsSkill portalOpsSkill : _portalOpsSkills) {
			skills.add(
				new SkillMetadataData(
					portalOpsSkill.getCapabilities(),
					portalOpsSkill.getDescription(),
					_toDomainName(portalOpsSkill.getName()),
					portalOpsSkill.getExamplePrompts(),
					portalOpsSkill.getName(),
					portalOpsSkill.getSupportedTools()));
		}

		skills.sort(Comparator.comparing(SkillMetadataData::getName));

		return List.copyOf(skills);
	}

	private List<ToolMetadataData> _getTools() {
		List<ToolMetadataData> tools = new ArrayList<>();

		for (PortalOpsTool portalOpsTool : _portalOpsTools) {
			tools.add(
				new ToolMetadataData(
					portalOpsTool.getCapabilities(),
					portalOpsTool.getDescription(),
					_toDomainName(portalOpsTool.getName()),
					portalOpsTool.getName()));
		}

		tools.sort(Comparator.comparing(ToolMetadataData::getName));

		return List.copyOf(tools);
	}

	private String _toDomainName(String name) {
		if (name.startsWith("PortalOps")) {
			return "PortalOps";
		}

		if (name.startsWith("User") || name.contains("Users")) {
			return "Users";
		}

		if (name.startsWith("Site") || name.contains("Sites") ||
			name.contains("Page")) {

			return "Sites";
		}

		if (name.startsWith("Content")) {
			return "Content";
		}

		if (name.startsWith("Search") || name.contains("Reindex")) {
			return "Search";
		}

		if (name.startsWith("Workflow")) {
			return "Workflow";
		}

		return "PortalOps";
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsAgent> _portalOpsAgents;

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsSkill> _portalOpsSkills;

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsTool> _portalOpsTools;

}
