package com.portalops.api.service;

import java.io.Serializable;
import java.util.Objects;

public class PortalOpsRequestContext implements Serializable {

    public PortalOpsRequestContext(long companyId, long groupId, long userId) {
        _companyId = companyId;
        _groupId = groupId;
        _userId = userId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public long getUserId() {
        return _userId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof PortalOpsRequestContext)) {
            return false;
        }

        PortalOpsRequestContext portalOpsRequestContext = (PortalOpsRequestContext) object;

        return (_companyId == portalOpsRequestContext._companyId) &&
                (_groupId == portalOpsRequestContext._groupId) &&
                (_userId == portalOpsRequestContext._userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_companyId, _groupId, _userId);
    }

    private final long _companyId;
    private final long _groupId;
    private final long _userId;

}