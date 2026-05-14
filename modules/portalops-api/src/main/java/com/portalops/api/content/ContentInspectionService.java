package com.portalops.api.content;

import com.portalops.api.service.PortalOpsRequestContext;

import java.util.List;

public interface ContentInspectionService {

    public List<ContentSummary> getStaleContent(
            PortalOpsRequestContext context);

    public List<ContentSummary> getUnpublishedDrafts(
            PortalOpsRequestContext context);

}