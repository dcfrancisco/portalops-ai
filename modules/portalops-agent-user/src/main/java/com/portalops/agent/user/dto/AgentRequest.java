package com.portalops.agent.user.dto;

import java.util.Objects;

public class AgentRequest {

	public AgentRequest(String prompt) {
		_prompt = Objects.requireNonNull(prompt);
	}

	public String getPrompt() {
		return _prompt;
	}

	private final String _prompt;

}