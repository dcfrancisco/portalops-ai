package com.portalops.ai.api;

import java.io.Serializable;
import java.util.Objects;

public class ActionLink implements Serializable {

    public ActionLink(String label, String screen, String anchor) {
        _anchor = Objects.requireNonNull(anchor);
        _label = Objects.requireNonNull(label);
        _screen = Objects.requireNonNull(screen);
    }

    public String getAnchor() {
        return _anchor;
    }

    public String getLabel() {
        return _label;
    }

    public String getScreen() {
        return _screen;
    }

    private final String _anchor;
    private final String _label;
    private final String _screen;

}
