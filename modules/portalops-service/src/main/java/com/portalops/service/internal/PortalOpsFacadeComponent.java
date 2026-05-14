package com.portalops.service.internal;

import com.portalops.api.command.CommandRouter;
import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.service.PortalOpsFacade;
import com.portalops.api.service.PortalOpsRequestContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PortalOpsFacade.class)
public class PortalOpsFacadeComponent implements PortalOpsFacade {

    @Override
    public PortalOpsCommandResult execute(PortalOpsCommandRequest commandRequest) {
        return _commandRouter.route(commandRequest);
    }

    @Override
    public PortalKnowledgeSnapshot getPortalKnowledgeSnapshot(
            PortalOpsRequestContext context) {

        return _portalKnowledgeService.getSnapshot(context);
    }

    @Reference
    private CommandRouter _commandRouter;

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

}