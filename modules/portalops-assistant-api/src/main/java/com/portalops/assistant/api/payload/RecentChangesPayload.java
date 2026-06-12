package com.portalops.assistant.api.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentChangesPayload implements AssistantPayload {

    public RecentChangesPayload(List<RecentChangeItem> recentChangeItems) {
        _recentChangeItems = Collections.unmodifiableList(
                new ArrayList<>(recentChangeItems));
    }

    public List<RecentChangeItem> getRecentChangeItems() {
        return _recentChangeItems;
    }

    private final List<RecentChangeItem> _recentChangeItems;

}
