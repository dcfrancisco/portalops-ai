package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ToolMetadataData implements Serializable {

	public ToolMetadataData(
		List<String> capabilities, String description, String domain,
		String name) {

		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_description = Objects.requireNonNull(description);
		_domain = Objects.requireNonNull(domain);
		_name = Objects.requireNonNull(name);
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

	private final List<String> _capabilities;
	private final String _description;
	private final String _domain;
	private final String _name;

}
