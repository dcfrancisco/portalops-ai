package com.portalops.agent.site.agent;

import com.portalops.agent.site.dto.AgentResponse;

public interface SiteAgent {

	public AgentResponse execute(String prompt);

	public String getName();

}
