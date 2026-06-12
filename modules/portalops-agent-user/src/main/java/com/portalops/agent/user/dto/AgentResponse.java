package com.portalops.agent.user.dto;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AgentResponse {

	public AgentResponse(boolean success, String message) {
		this(success, message, List.of(), List.of());
	}

	public AgentResponse(
		boolean success, String message, List<String> findings,
		List<String> executionPath) {

		_executionPath = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(executionPath)));
		_findings = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(findings)));
		_message = Objects.requireNonNull(message);
		_success = success;
	}

	public List<String> getExecutionPath() {
		return _executionPath;
	}

	public List<String> getFindings() {
		return _findings;
	}

	public String getMessage() {
		return _message;
	}

	public boolean isSuccess() {
		return _success;
	}

	private final List<String> _executionPath;
	private final List<String> _findings;
	private final String _message;
	private final boolean _success;

}
