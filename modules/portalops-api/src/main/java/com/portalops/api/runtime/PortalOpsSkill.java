package com.portalops.api.runtime;

import java.util.List;

public interface PortalOpsSkill {

	public List<String> getCapabilities();

	public String getDescription();

	public List<String> getExamplePrompts();

	public String getName();

	public List<String> getSupportedTools();

}
