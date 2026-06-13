package com.portalops.agent.search.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AgentResponse implements Serializable {

	public static AgentResponse failure(String errorCode) {
		return new AgentResponse(null, errorCode, List.of(), false);
	}

	public static AgentResponse success(
		Object data, List<String> executionPath) {

		return new AgentResponse(data, null, executionPath, true);
	}

	public Object getData() {
		return _data;
	}

	public String getErrorCode() {
		return _errorCode;
	}

	public List<String> getExecutionPath() {
		return _executionPath;
	}

	public boolean isSuccess() {
		return _success;
	}

	private AgentResponse(
		Object data, String errorCode, List<String> executionPath,
		boolean success) {

		_data = data;
		_errorCode = errorCode;
		_executionPath = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(executionPath)));
		_success = success;
	}

	private final Object _data;
	private final String _errorCode;
	private final List<String> _executionPath;
	private final boolean _success;

}
