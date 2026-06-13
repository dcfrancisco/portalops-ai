package com.portalops.api.search;

import java.io.Serializable;
import java.util.Objects;

public class SearchDiagnostic implements Serializable {

	public SearchDiagnostic(
		String description, String severity, String title) {

		_description = Objects.requireNonNull(description);
		_severity = Objects.requireNonNull(severity);
		_title = Objects.requireNonNull(title);
	}

	public String getDescription() {
		return _description;
	}

	public String getSeverity() {
		return _severity;
	}

	public String getTitle() {
		return _title;
	}

	private final String _description;
	private final String _severity;
	private final String _title;

}
