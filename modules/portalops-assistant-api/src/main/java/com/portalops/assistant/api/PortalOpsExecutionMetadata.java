package com.portalops.assistant.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsExecutionMetadata implements Serializable {

	public PortalOpsExecutionMetadata(
		List<String> executionPath, List<String> findings, String summary) {

		_executionPath = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(executionPath)));
		_findings = Collections.unmodifiableList(
			new ArrayList<>(Objects.requireNonNull(findings)));
		_summary = Objects.requireNonNull(summary);
	}

	public List<String> getExecutionPath() {
		return _executionPath;
	}

	public List<String> getFindings() {
		return _findings;
	}

	public String getSummary() {
		return _summary;
	}

	private final List<String> _executionPath;
	private final List<String> _findings;
	private final String _summary;

}
