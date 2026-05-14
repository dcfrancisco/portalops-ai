package com.portalops.api.workflow;

import java.io.Serializable;
import java.util.Objects;

public class WorkflowTaskSummary implements Serializable {

    public WorkflowTaskSummary(String assigneeName, String assetTitle,
            String workflowDefinitionName, int workflowDefinitionVersion,
            long workflowInstanceId, long workflowTaskId, String workflowTaskName) {

        _assigneeName = Objects.requireNonNull(assigneeName);
        _assetTitle = Objects.requireNonNull(assetTitle);
        _workflowDefinitionName = Objects.requireNonNull(workflowDefinitionName);
        _workflowDefinitionVersion = workflowDefinitionVersion;
        _workflowInstanceId = workflowInstanceId;
        _workflowTaskId = workflowTaskId;
        _workflowTaskName = Objects.requireNonNull(workflowTaskName);
    }

    public String getAssigneeName() {
        return _assigneeName;
    }

    public String getAssetTitle() {
        return _assetTitle;
    }

    public String getWorkflowDefinitionLabel() {
        return _workflowDefinitionName + " v" + _workflowDefinitionVersion;
    }

    public String getWorkflowDefinitionName() {
        return _workflowDefinitionName;
    }

    public int getWorkflowDefinitionVersion() {
        return _workflowDefinitionVersion;
    }

    public long getWorkflowInstanceId() {
        return _workflowInstanceId;
    }

    public long getWorkflowTaskId() {
        return _workflowTaskId;
    }

    public String getWorkflowTaskName() {
        return _workflowTaskName;
    }

    private final String _assigneeName;
    private final String _assetTitle;
    private final String _workflowDefinitionName;
    private final int _workflowDefinitionVersion;
    private final long _workflowInstanceId;
    private final long _workflowTaskId;
    private final String _workflowTaskName;

}