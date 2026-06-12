package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsDashboardSection implements Serializable {

    public PortalOpsDashboardSection(
            String id, String title, String description,
            List<PortalOpsDashboardCard> cards) {

        _cards = Collections.unmodifiableList(new ArrayList<>(cards));
        _description = Objects.requireNonNull(description);
        _id = Objects.requireNonNull(id);
        _title = Objects.requireNonNull(title);
    }

    public List<PortalOpsDashboardCard> getCards() {
        return _cards;
    }

    public String getDescription() {
        return _description;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    private final List<PortalOpsDashboardCard> _cards;
    private final String _description;
    private final String _id;
    private final String _title;

}
