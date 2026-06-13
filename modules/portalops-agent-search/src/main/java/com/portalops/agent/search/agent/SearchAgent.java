package com.portalops.agent.search.agent;

import com.portalops.agent.search.dto.AgentResponse;

public interface SearchAgent {

	public AgentResponse execute(String prompt);

	public String getName();

}
