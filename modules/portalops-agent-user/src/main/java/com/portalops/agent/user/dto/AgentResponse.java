package com.portalops.agent.user.dto;

import java.util.Objects;

public class AgentResponse {

	public AgentResponse(boolean success, String message) {
		_message = Objects.requireNonNull(message);
		_success = success;
	}

	public String getMessage() {
		return _message;
	}

	public boolean isSuccess() {
		return _success;
	}

	private final String _message;
	private final boolean _success;

}
