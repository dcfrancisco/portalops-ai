package com.portalops.api.knowledge;

import com.portalops.api.workflow.WorkflowSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkflowKnowledge implements Serializable {

    public WorkflowKnowledge(List<WorkflowSummary> pendingWorkflows,
            List<WorkflowSummary> stuckWorkflows) {

        _pendingWorkflows = Collections.unmodifiableList(
                new ArrayList<>(pendingWorkflows));
        _stuckWorkflows = Collections.unmodifiableList(
                new ArrayList<>(stuckWorkflows));
    }

    public List<WorkflowSummary> getPendingWorkflows() {
        return _pendingWorkflows;
    }

    public List<WorkflowSummary> getStuckWorkflows() {
        return _stuckWorkflows;
    }

    private final List<WorkflowSummary> _pendingWorkflows;
    private final List<WorkflowSummary> _stuckWorkflows;

}