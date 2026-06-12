package com.portalops.assistant.api;

import java.io.Serializable;
import java.util.Objects;

public class AssistantAction implements Serializable {

    public AssistantAction(String label, String value) {
        _label = Objects.requireNonNull(label);
        _value = Objects.requireNonNull(value);
    }

    public String getLabel() {
        return _label;
    }

    public String getValue() {
        return _value;
    }

    private final String _label;
    private final String _value;

}
