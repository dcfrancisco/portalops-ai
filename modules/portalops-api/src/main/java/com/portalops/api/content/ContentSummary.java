package com.portalops.api.content;

import java.io.Serializable;
import java.util.Objects;

public class ContentSummary implements Serializable {

    public ContentSummary(long id, String title, String status, String type) {
        _id = id;
        _status = Objects.requireNonNull(status);
        _title = Objects.requireNonNull(title);
        _type = Objects.requireNonNull(type);
    }

    public long getId() {
        return _id;
    }

    public String getStatus() {
        return _status;
    }

    public String getTitle() {
        return _title;
    }

    public String getType() {
        return _type;
    }

    private final long _id;
    private final String _status;
    private final String _title;
    private final String _type;

}