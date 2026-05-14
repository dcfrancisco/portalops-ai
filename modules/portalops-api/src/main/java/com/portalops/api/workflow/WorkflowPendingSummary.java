package com.portalops.api.workflow;

import java.io.Serializable;
import java.util.Objects;

public class WorkflowPendingSummary implements Serializable {

    public WorkflowPendingSummary(String key, String label, int taskCount) {
        _key = Objects.requireNonNull(key);
        _label = Objects.requireNonNull(label);
        _taskCount = taskCount;
    }

    public String getKey() {
        return _key;
    }

    public String getLabel() {
        return _label;
    }

    public int getTaskCount() {
        return _taskCount;
    }

    private final String _key;
    private final String _label;
    private final int _taskCount;

}