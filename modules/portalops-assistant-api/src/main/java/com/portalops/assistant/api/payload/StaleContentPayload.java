package com.portalops.assistant.api.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaleContentPayload implements AssistantPayload {

    public StaleContentPayload(List<StaleContentItem> staleContentItems) {
        _staleContentItems = Collections.unmodifiableList(
                new ArrayList<>(staleContentItems));
    }

    public List<StaleContentItem> getStaleContentItems() {
        return _staleContentItems;
    }

    private final List<StaleContentItem> _staleContentItems;

}
