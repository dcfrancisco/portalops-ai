package com.portalops.ai.api;

import java.io.Serializable;
import java.util.Objects;

public class Recommendation implements Serializable {

    public Recommendation(String title, String description) {
        _description = Objects.requireNonNull(description);
        _title = Objects.requireNonNull(title);
    }

    public String getDescription() {
        return _description;
    }

    public String getTitle() {
        return _title;
    }

    private final String _description;
    private final String _title;

}
