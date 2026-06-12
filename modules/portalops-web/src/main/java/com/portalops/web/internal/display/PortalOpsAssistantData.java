package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsAssistantData implements Serializable {

    public PortalOpsAssistantData(
            String title, String description, String placeholder,
            String actionLabel, List<String> suggestedPrompts,
            PortalOpsAssistantInsight insight) {

        _actionLabel = Objects.requireNonNull(actionLabel);
        _description = Objects.requireNonNull(description);
        _insight = Objects.requireNonNull(insight);
        _placeholder = Objects.requireNonNull(placeholder);
        _suggestedPrompts = Collections.unmodifiableList(
                new ArrayList<>(suggestedPrompts));
        _title = Objects.requireNonNull(title);
    }

    public String getActionLabel() {
        return _actionLabel;
    }

    public String getDescription() {
        return _description;
    }

    public PortalOpsAssistantInsight getInsight() {
        return _insight;
    }

    public String getPlaceholder() {
        return _placeholder;
    }

    public List<String> getSuggestedPrompts() {
        return _suggestedPrompts;
    }

    public String getTitle() {
        return _title;
    }

    private final String _actionLabel;
    private final String _description;
    private final PortalOpsAssistantInsight _insight;
    private final String _placeholder;
    private final List<String> _suggestedPrompts;
    private final String _title;

}
