package com.portalops.web.internal.dashboard;

import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.web.internal.display.PortalOpsDashboardData;

public interface PortalOpsDashboardDataProvider {

    public PortalOpsDashboardData getPortalOpsDashboardData(
            PortalOpsRequestContext portalOpsRequestContext,
            PortalKnowledgeSnapshot portalKnowledgeSnapshot);

}
