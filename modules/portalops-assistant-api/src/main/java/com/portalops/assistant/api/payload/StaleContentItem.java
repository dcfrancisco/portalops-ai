package com.portalops.assistant.api.payload;

import java.io.Serializable;
import java.util.Objects;

public class StaleContentItem implements Serializable {

    public StaleContentItem(String title, String type, String status) {
        _status = Objects.requireNonNull(status);
        _title = Objects.requireNonNull(title);
        _type = Objects.requireNonNull(type);
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

    private final String _status;
    private final String _title;
    private final String _type;

}
