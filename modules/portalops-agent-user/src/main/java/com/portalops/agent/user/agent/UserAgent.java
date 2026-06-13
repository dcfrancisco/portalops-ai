package com.portalops.agent.user.agent;

import com.portalops.agent.user.dto.AgentResponse;
import com.portalops.api.runtime.PortalOpsAgent;

public interface UserAgent extends PortalOpsAgent {

	public AgentResponse execute(String prompt);

}