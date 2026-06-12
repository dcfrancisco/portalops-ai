package com.portalops.assistant.api.payload;

import java.io.Serializable;
import java.util.Objects;

public class FailedWorkflowItem implements Serializable {

    public FailedWorkflowItem(
            String assetTitle, String workflowDefinition, String status) {

        _assetTitle = Objects.requireNonNull(assetTitle);
        _status = Objects.requireNonNull(status);
        _workflowDefinition = Objects.requireNonNull(workflowDefinition);
    }

    public String getAssetTitle() {
        return _assetTitle;
    }

    public String getStatus() {
        return _status;
    }

    public String getWorkflowDefinition() {
        return _workflowDefinition;
    }

    private final String _assetTitle;
    private final String _status;
    private final String _workflowDefinition;

}
