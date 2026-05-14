package com.portalops.api.workflow;

import java.io.Serializable;
import java.util.Objects;

public class WorkflowSummary implements Serializable {

    public WorkflowSummary(
            long id, String title, String status, String assigneeName) {

        _assigneeName = Objects.requireNonNull(assigneeName);
        _id = id;
        _status = Objects.requireNonNull(status);
        _title = Objects.requireNonNull(title);
    }

    public String getAssigneeName() {
        return _assigneeName;
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

    private final String _assigneeName;
    private final long _id;
    private final String _status;
    private final String _title;

}