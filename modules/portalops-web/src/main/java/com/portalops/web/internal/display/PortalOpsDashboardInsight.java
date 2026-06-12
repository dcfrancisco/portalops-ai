package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsDashboardInsight implements Serializable {

    public PortalOpsDashboardInsight(
            String title, String description, String severity,
            String actionLabel, String actionScreen, String actionAnchor) {

        _actionAnchor = Objects.requireNonNull(actionAnchor);
        _actionLabel = Objects.requireNonNull(actionLabel);
        _actionScreen = Objects.requireNonNull(actionScreen);
        _description = Objects.requireNonNull(description);
        _severity = Objects.requireNonNull(severity);
        _title = Objects.requireNonNull(title);
    }

    public String getActionAnchor() {
        return _actionAnchor;
    }

    public String getActionLabel() {
        return _actionLabel;
    }

    public String getActionScreen() {
        return _actionScreen;
    }

    public String getDescription() {
        return _description;
    }

    public String getSeverity() {
        return _severity;
    }

    public String getTitle() {
        return _title;
    }

    private final String _actionAnchor;
    private final String _actionLabel;
    private final String _actionScreen;
    private final String _description;
    private final String _severity;
    private final String _title;

}
