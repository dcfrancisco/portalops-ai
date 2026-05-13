package com.portalops.ai.module.portal;

import com.portalops.ai.adapter.out.liferay.LiferayGateway;
import com.portalops.ai.application.command.CapabilityModule;
import com.portalops.ai.application.command.CommandResponse;
import com.portalops.ai.application.policy.AccessContext;
import com.portalops.ai.domain.command.CapabilityId;
import com.portalops.ai.domain.command.CommandIntent;

import org.springframework.stereotype.Component;

@Component
public class PortalManagementModule implements CapabilityModule {

    private final LiferayGateway liferayGateway;

    public PortalManagementModule(LiferayGateway liferayGateway) {
        this.liferayGateway = liferayGateway;
    }

    @Override
    public CapabilityId capability() {
        return CapabilityId.PORTAL_MANAGEMENT;
    }

    @Override
    public boolean supports(CommandIntent commandIntent) {
        return commandIntent.capability() == capability();
    }

    @Override
    public CommandResponse handle(CommandIntent commandIntent, AccessContext accessContext) {
        return new CommandResponse(
                capability().name(),
                commandIntent.operation(),
                "Portal management overview prepared for " + accessContext.actorId(),
                liferayGateway.loadPortalOverview());
    }
}