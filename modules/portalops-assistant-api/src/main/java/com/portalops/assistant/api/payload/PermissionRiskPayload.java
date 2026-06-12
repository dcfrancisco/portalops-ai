package com.portalops.assistant.api.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionRiskPayload implements AssistantPayload {

    public PermissionRiskPayload(List<PermissionRiskItem> permissionRiskItems) {
        _permissionRiskItems = Collections.unmodifiableList(
                new ArrayList<>(permissionRiskItems));
    }

    public List<PermissionRiskItem> getPermissionRiskItems() {
        return _permissionRiskItems;
    }

    private final List<PermissionRiskItem> _permissionRiskItems;

}
