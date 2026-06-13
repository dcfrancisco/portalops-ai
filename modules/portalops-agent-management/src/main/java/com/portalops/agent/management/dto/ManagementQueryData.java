package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ManagementQueryData implements Serializable {

	public static final String TYPE_DESCRIBE_CAPABILITY = "describe-capability";
	public static final String TYPE_LIST_AGENTS = "list-agents";
	public static final String TYPE_LIST_CAPABILITIES = "list-capabilities";
	public static final String TYPE_LIST_DOMAINS = "list-domains";
	public static final String TYPE_LIST_SKILLS = "list-skills";

	public ManagementQueryData(
		List<AgentMetadataData> agents,
		List<CapabilityMetadataData> capabilities,
		List<DomainMetadataData> domains, String queryType,
		List<SkillMetadataData> skills, String subject,
		int totalAgents, int totalCapabilities, int totalDomains,
		int totalSkills) {

		_agents = List.copyOf(Objects.requireNonNull(agents));
		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_domains = List.copyOf(Objects.requireNonNull(domains));
		_queryType = Objects.requireNonNull(queryType);
		_skills = List.copyOf(Objects.requireNonNull(skills));
		_subject = subject;
		_totalAgents = totalAgents;
		_totalCapabilities = totalCapabilities;
		_totalDomains = totalDomains;
		_totalSkills = totalSkills;
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

	public String getQueryType() {
		return _queryType;
	}

	public List<SkillMetadataData> getSkills() {
		return _skills;
	}

	public String getSubject() {
		return _subject;
	}

	public int getTotalAgents() {
		return _totalAgents;
	}

	public int getTotalCapabilities() {
		return _totalCapabilities;
	}

	public int getTotalDomains() {
		return _totalDomains;
	}

	public int getTotalSkills() {
		return _totalSkills;
	}

	private final List<AgentMetadataData> _agents;
	private final List<CapabilityMetadataData> _capabilities;
	private final List<DomainMetadataData> _domains;
	private final String _queryType;
	private final List<SkillMetadataData> _skills;
	private final String _subject;
	private final int _totalAgents;
	private final int _totalCapabilities;
	private final int _totalDomains;
	private final int _totalSkills;

}
