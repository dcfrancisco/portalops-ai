package com.portalops.api.site;

import java.io.Serializable;
import java.util.Objects;

public class SiteSummary implements Serializable {

    public SiteSummary(
            boolean active, String friendlyURL, long groupId,
            String modifiedDate, int privatePages, int publicPages,
            String title, int userCount) {

        _active = active;
        _friendlyURL = Objects.requireNonNull(friendlyURL);
        _groupId = groupId;
        _modifiedDate = modifiedDate;
        _privatePages = privatePages;
        _publicPages = publicPages;
        _title = Objects.requireNonNull(title);
        _userCount = userCount;
    }

    public String getFriendlyURL() {
        return _friendlyURL;
    }

    public long getGroupId() {
        return _groupId;
    }

    public String getModifiedDate() {
        return _modifiedDate;
    }

    public int getPrivatePages() {
        return _privatePages;
    }

    public int getPublicPages() {
        return _publicPages;
    }

    public String getTitle() {
        return _title;
    }

    public int getUserCount() {
        return _userCount;
    }

    public boolean isActive() {
        return _active;
    }

    private final boolean _active;
    private final String _friendlyURL;
    private final long _groupId;
    private final String _modifiedDate;
    private final int _privatePages;
    private final int _publicPages;
    private final String _title;
    private final int _userCount;

}
