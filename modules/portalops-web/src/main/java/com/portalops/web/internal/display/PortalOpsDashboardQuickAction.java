package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsDashboardQuickAction implements Serializable {

    public PortalOpsDashboardQuickAction(
            String label, String icon, String screen, String anchor,
            boolean primary) {

        _anchor = Objects.requireNonNull(anchor);
        _icon = Objects.requireNonNull(icon);
        _label = Objects.requireNonNull(label);
        _primary = primary;
        _screen = Objects.requireNonNull(screen);
    }

    public String getAnchor() {
        return _anchor;
    }

    public String getIcon() {
        return _icon;
    }

    public String getLabel() {
        return _label;
    }

    public String getScreen() {
        return _screen;
    }

    public boolean isPrimary() {
        return _primary;
    }

    private final String _anchor;
    private final String _icon;
    private final String _label;
    private final boolean _primary;
    private final String _screen;

}
