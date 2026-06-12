package com.portalops.assistant.api.payload;

import java.io.Serializable;
import java.util.Objects;

public class RecentChangeItem implements Serializable {

    public RecentChangeItem(
            String category, String description, String happenedAt) {

        _category = Objects.requireNonNull(category);
        _description = Objects.requireNonNull(description);
        _happenedAt = Objects.requireNonNull(happenedAt);
    }

    public String getCategory() {
        return _category;
    }

    public String getDescription() {
        return _description;
    }

    public String getHappenedAt() {
        return _happenedAt;
    }

    private final String _category;
    private final String _description;
    private final String _happenedAt;

}
