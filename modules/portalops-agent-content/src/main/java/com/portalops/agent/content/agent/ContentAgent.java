package com.portalops.agent.content.agent;

import com.portalops.agent.content.dto.AgentResponse;

public interface ContentAgent {

	public AgentResponse execute(String prompt);

	public String getName();

}
