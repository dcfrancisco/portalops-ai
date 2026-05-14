package com.portalops.api.site;

import java.io.Serializable;
import java.util.Objects;

public class SiteFinding implements Serializable {

    public SiteFinding(String category, String detail, String title) {
        _category = Objects.requireNonNull(category);
        _detail = Objects.requireNonNull(detail);
        _title = Objects.requireNonNull(title);
    }

    public String getCategory() {
        return _category;
    }

    public String getDetail() {
        return _detail;
    }

    public String getTitle() {
        return _title;
    }

    private final String _category;
    private final String _detail;
    private final String _title;

}