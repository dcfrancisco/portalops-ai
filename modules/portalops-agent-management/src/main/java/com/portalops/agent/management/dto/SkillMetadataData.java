package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SkillMetadataData implements Serializable {

	public SkillMetadataData(
		List<String> capabilities, String description, String domain,
		List<String> examplePrompts, String name,
		List<String> supportedTools) {

		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_description = Objects.requireNonNull(description);
		_domain = Objects.requireNonNull(domain);
		_examplePrompts = List.copyOf(Objects.requireNonNull(examplePrompts));
		_name = Objects.requireNonNull(name);
		_supportedTools = List.copyOf(Objects.requireNonNull(supportedTools));
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

	public List<String> getExamplePrompts() {
		return _examplePrompts;
	}

	public String getName() {
		return _name;
	}

	public List<String> getSupportedTools() {
		return _supportedTools;
	}

	private final List<String> _capabilities;
	private final String _description;
	private final String _domain;
	private final List<String> _examplePrompts;
	private final String _name;
	private final List<String> _supportedTools;

}
