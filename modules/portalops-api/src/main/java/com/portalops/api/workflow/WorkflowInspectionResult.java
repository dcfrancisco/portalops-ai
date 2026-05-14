package com.portalops.api.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WorkflowInspectionResult implements Serializable {

    public WorkflowInspectionResult(List<WorkflowPendingSummary> pendingByAssignee,
            List<WorkflowPendingSummary> pendingByWorkflowDefinition,
            String scopeGroupName, List<WorkflowTaskSummary> workflowTaskSummaries) {

        _pendingByAssignee = Collections.unmodifiableList(
                new ArrayList<>(pendingByAssignee));
        _pendingByWorkflowDefinition = Collections.unmodifiableList(
                new ArrayList<>(pendingByWorkflowDefinition));
        _scopeGroupName = Objects.requireNonNull(scopeGroupName);
        _workflowTaskSummaries = Collections.unmodifiableList(
                new ArrayList<>(workflowTaskSummaries));
    }

    public List<WorkflowPendingSummary> getPendingByAssignee() {
        return _pendingByAssignee;
    }

    public List<WorkflowPendingSummary> getPendingByWorkflowDefinition() {
        return _pendingByWorkflowDefinition;
    }

    public int getPendingTaskCount() {
        return _workflowTaskSummaries.size();
    }

    public String getScopeGroupName() {
        return _scopeGroupName;
    }

    public List<WorkflowTaskSummary> getWorkflowTaskSummaries() {
        return _workflowTaskSummaries;
    }

    private final List<WorkflowPendingSummary> _pendingByAssignee;
    private final List<WorkflowPendingSummary> _pendingByWorkflowDefinition;
    private final String _scopeGroupName;
    private final List<WorkflowTaskSummary> _workflowTaskSummaries;

}