package com.portalops.assistant.api.payload;

import java.io.Serializable;
import java.util.Objects;

public class SearchIssue implements Serializable {

    public SearchIssue(String title, String severity, String description) {
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
