package com.portalops.agent.user.agent;

import com.portalops.agent.user.dto.AgentResponse;

public interface UserAgent {

	public AgentResponse execute(String prompt);

}
