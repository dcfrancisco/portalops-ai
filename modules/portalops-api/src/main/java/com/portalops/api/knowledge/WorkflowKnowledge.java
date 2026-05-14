package com.portalops.api.knowledge;

import com.portalops.api.workflow.WorkflowInspectionResult;
import com.portalops.api.workflow.WorkflowSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WorkflowKnowledge implements Serializable {

    public WorkflowKnowledge(WorkflowInspectionResult pendingWorkflowInspectionResult,
            List<WorkflowSummary> stuckWorkflows) {

        _pendingWorkflowInspectionResult = Objects.requireNonNull(
                pendingWorkflowInspectionResult);
        _stuckWorkflows = Collections.unmodifiableList(
                new ArrayList<>(stuckWorkflows));
    }

    public WorkflowInspectionResult getPendingWorkflowInspectionResult() {
        return _pendingWorkflowInspectionResult;
    }

    public List<WorkflowSummary> getStuckWorkflows() {
        return _stuckWorkflows;
    }

    private final WorkflowInspectionResult _pendingWorkflowInspectionResult;
    private final List<WorkflowSummary> _stuckWorkflows;

}