package com.portalops.assistant.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsExecutionMetadata implements Serializable {

	public PortalOpsExecutionMetadata(
		String dataJSON, List<String> executionPath) {

		_dataJSON = Objects.requireNonNull(dataJSON);
		_executionPath = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(executionPath)));
	}

	public String getDataJSON() {
		return _dataJSON;
	}

	public List<String> getExecutionPath() {
		return _executionPath;
	}

	private final String _dataJSON;
	private final List<String> _executionPath;

}
