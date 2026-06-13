package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AgentMetadataData implements Serializable {

	public AgentMetadataData(
		List<String> capabilities, String description, String domain,
		String name, List<String> supportedSkills) {

		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_description = Objects.requireNonNull(description);
		_domain = Objects.requireNonNull(domain);
		_name = Objects.requireNonNull(name);
		_supportedSkills = List.copyOf(Objects.requireNonNull(supportedSkills));
	}

	public List<String> getCapabilities() {
		return _capabilities;
	}

	public String getDescription() {
		return _description;
	}

	public String getDomain() {
		return _domain;
	}

	public String getName() {
		return _name;
	}

	public List<String> getSupportedSkills() {
		return _supportedSkills;
	}

	private final List<String> _capabilities;
	private final String _description;
	private final String _domain;
	private final String _name;
	private final List<String> _supportedSkills;

}
