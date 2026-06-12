package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsNavigationItem implements Serializable {

    public PortalOpsNavigationItem(String icon, String label, String screen) {
        _icon = Objects.requireNonNull(icon);
        _label = Objects.requireNonNull(label);
        _screen = Objects.requireNonNull(screen);
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

    private final String _icon;
    private final String _label;
    private final String _screen;

}
