package com.portalops.ai.application.command;

import com.portalops.ai.application.policy.AccessContext;
import com.portalops.ai.domain.command.CapabilityId;
import com.portalops.ai.domain.command.CommandIntent;

public interface CapabilityModule {

    CapabilityId capability();

    boolean supports(CommandIntent commandIntent);

    CommandResponse handle(CommandIntent commandIntent, AccessContext accessContext);
}