package com.portalops.ai.api;

import java.io.Serializable;
import java.util.Objects;

public class FindingCard implements Serializable {

    public FindingCard(
            String title, String value, String status, String summary) {

        _status = Objects.requireNonNull(status);
        _summary = Objects.requireNonNull(summary);
        _title = Objects.requireNonNull(title);
        _value = Objects.requireNonNull(value);
    }

    public String getStatus() {
        return _status;
    }

    public String getSummary() {
        return _summary;
    }

    public String getTitle() {
        return _title;
    }

    public String getValue() {
        return _value;
    }

    private final String _status;
    private final String _summary;
    private final String _title;
    private final String _value;

}
