package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.Objects;

public class CapabilityMetadataData implements Serializable {

	public CapabilityMetadataData(
		String domain, String name, String ownerName, String ownerType) {

		_domain = Objects.requireNonNull(domain);
		_name = Objects.requireNonNull(name);
		_ownerName = Objects.requireNonNull(ownerName);
		_ownerType = Objects.requireNonNull(ownerType);
	}

	public String getDomain() {
		return _domain;
	}

	public String getName() {
		return _name;
	}

	public String getOwnerName() {
		return _ownerName;
	}

	public String getOwnerType() {
		return _ownerType;
	}

	private final String _domain;
	private final String _name;
	private final String _ownerName;
	private final String _ownerType;

}
