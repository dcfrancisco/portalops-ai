package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsDashboardCard implements Serializable {

    public PortalOpsDashboardCard(
            String title, String value, String status, String context,
            String note) {

        _context = Objects.requireNonNull(context);
        _note = Objects.requireNonNull(note);
        _status = Objects.requireNonNull(status);
        _title = Objects.requireNonNull(title);
        _value = Objects.requireNonNull(value);
    }

    public String getContext() {
        return _context;
    }

    public String getNote() {
        return _note;
    }

    public String getStatus() {
        return _status;
    }

    public String getTitle() {
        return _title;
    }

    public String getValue() {
        return _value;
    }

    private final String _context;
    private final String _note;
    private final String _status;
    private final String _title;
    private final String _value;

}
