package com.portalops.assistant.api;

import com.portalops.assistant.api.payload.AssistantPayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsAssistantResponse<T extends AssistantPayload>
        implements Serializable {

    public PortalOpsAssistantResponse(
            AssistantStatus status, String title, String summary,
            List<String> findings, List<String> recommendations,
            List<AssistantAction> actions, T payload) {

        _actions = Collections.unmodifiableList(new ArrayList<>(actions));
        _findings = Collections.unmodifiableList(new ArrayList<>(findings));
        _payload = payload;
        _recommendations = Collections.unmodifiableList(
                new ArrayList<>(recommendations));
        _status = Objects.requireNonNull(status);
        _summary = Objects.requireNonNull(summary);
        _title = Objects.requireNonNull(title);
    }

    public List<AssistantAction> getActions() {
        return _actions;
    }

    public List<String> getFindings() {
        return _findings;
    }

    public T getPayload() {
        return _payload;
    }

    public List<String> getRecommendations() {
        return _recommendations;
    }

    public AssistantStatus getStatus() {
        return _status;
    }

    public String getSummary() {
        return _summary;
    }

    public String getTitle() {
        return _title;
    }

    private final List<AssistantAction> _actions;
    private final List<String> _findings;
    private final T _payload;
    private final List<String> _recommendations;
    private final AssistantStatus _status;
    private final String _summary;
    private final String _title;

}
