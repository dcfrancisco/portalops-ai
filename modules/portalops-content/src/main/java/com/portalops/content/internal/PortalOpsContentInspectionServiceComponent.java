package com.portalops.content.internal;

import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.content.ContentSummary;
import com.portalops.api.service.PortalOpsRequestContext;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = ContentInspectionService.class)
public class PortalOpsContentInspectionServiceComponent
        implements ContentInspectionService {

    @Override
    public List<ContentSummary> getStaleContent(PortalOpsRequestContext context) {
        return Collections.emptyList();
    }

    @Override
    public List<ContentSummary> getUnpublishedDrafts(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

}