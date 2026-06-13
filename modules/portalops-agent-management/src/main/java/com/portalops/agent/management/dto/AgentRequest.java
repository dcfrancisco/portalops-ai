package com.portalops.agent.management.dto;

import java.io.Serializable;
import java.util.Objects;

public class AgentRequest implements Serializable {

	public AgentRequest(String prompt) {
		_prompt = Objects.requireNonNull(prompt);
	}

	public String getPrompt() {
		return _prompt;
	}

	private final String _prompt;

}
