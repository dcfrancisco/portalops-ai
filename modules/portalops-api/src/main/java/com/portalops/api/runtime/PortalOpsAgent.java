package com.portalops.api.runtime;

import java.util.List;

public interface PortalOpsAgent {

	public List<String> getCapabilities();

	public String getDescription();

	public String getName();

	public List<String> getSupportedSkills();

}
