package com.portalops.api.service;

import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;

public interface PortalOpsFacade {

    public PortalOpsCommandResult execute(PortalOpsCommandRequest commandRequest);

    public PortalKnowledgeSnapshot getPortalKnowledgeSnapshot(
            PortalOpsRequestContext context);

}