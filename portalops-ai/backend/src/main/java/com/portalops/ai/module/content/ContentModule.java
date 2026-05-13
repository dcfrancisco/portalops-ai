package com.portalops.ai.module.content;

import com.portalops.ai.adapter.out.liferay.LiferayGateway;
import com.portalops.ai.application.command.CapabilityModule;
import com.portalops.ai.application.command.CommandResponse;
import com.portalops.ai.application.policy.AccessContext;
import com.portalops.ai.domain.command.CapabilityId;
import com.portalops.ai.domain.command.CommandIntent;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContentModule implements CapabilityModule {

    private final LiferayGateway liferayGateway;

    public ContentModule(LiferayGateway liferayGateway) {
        this.liferayGateway = liferayGateway;
    }

    @Override
    public CapabilityId capability() {
        return CapabilityId.CONTENT;
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
                "Content hygiene findings prepared for " + commandIntent.qualifier(),
                Map.of("items", liferayGateway.loadStaleContent()));
    }
}