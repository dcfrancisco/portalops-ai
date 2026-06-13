package com.portalops.agent.management.agent;

import com.portalops.agent.management.dto.AgentResponse;

public interface ManagementAgent {

	public AgentResponse execute(String prompt);

	public String getName();

}
