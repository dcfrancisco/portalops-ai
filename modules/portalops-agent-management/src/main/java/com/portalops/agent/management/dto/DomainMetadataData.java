package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DomainMetadataData implements Serializable {

	public DomainMetadataData(
		String description, String name, List<String> capabilities) {

		_capabilities = List.copyOf(Objects.requireNonNull(capabilities));
		_description = Objects.requireNonNull(description);
		_name = Objects.requireNonNull(name);
	}

	public List<String> getCapabilities() {
		return _capabilities;
	}

	public String getDescription() {
		return _description;
	}

	public String getName() {
		return _name;
	}

	private final List<String> _capabilities;
	private final String _description;
	private final String _name;

}
