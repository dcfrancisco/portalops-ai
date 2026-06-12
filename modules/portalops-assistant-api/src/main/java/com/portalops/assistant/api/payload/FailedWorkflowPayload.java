package com.portalops.assistant.api.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FailedWorkflowPayload implements AssistantPayload {

    public FailedWorkflowPayload(List<FailedWorkflowItem> failedWorkflowItems) {
        _failedWorkflowItems = Collections.unmodifiableList(
                new ArrayList<>(failedWorkflowItems));
    }

    public List<FailedWorkflowItem> getFailedWorkflowItems() {
        return _failedWorkflowItems;
    }

    private final List<FailedWorkflowItem> _failedWorkflowItems;

}
