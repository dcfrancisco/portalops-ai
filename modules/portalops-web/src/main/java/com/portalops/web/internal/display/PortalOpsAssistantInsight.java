package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsAssistantInsight implements Serializable {

    public PortalOpsAssistantInsight(
            String title, String status, List<String> findings,
            List<String> recommendations) {

        _findings = Collections.unmodifiableList(new ArrayList<>(findings));
        _recommendations = Collections.unmodifiableList(
                new ArrayList<>(recommendations));
        _status = Objects.requireNonNull(status);
        _title = Objects.requireNonNull(title);
    }

    public List<String> getFindings() {
        return _findings;
    }

    public List<String> getRecommendations() {
        return _recommendations;
    }

    public String getStatus() {
        return _status;
    }

    public String getTitle() {
        return _title;
    }

    private final List<String> _findings;
    private final List<String> _recommendations;
    private final String _status;
    private final String _title;

}
