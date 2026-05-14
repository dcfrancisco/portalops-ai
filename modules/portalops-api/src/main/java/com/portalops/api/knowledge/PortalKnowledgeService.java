package com.portalops.api.knowledge;

import com.portalops.api.service.PortalOpsRequestContext;

public interface PortalKnowledgeService {

    public PortalKnowledgeSnapshot getSnapshot(PortalOpsRequestContext context);

}