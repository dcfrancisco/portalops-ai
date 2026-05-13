package com.portalops.ai.application.command;

import com.portalops.ai.application.policy.AccessContext;
import com.portalops.ai.application.policy.PermissionGate;
import com.portalops.ai.domain.command.CommandIntent;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandRouterService {

    private final List<CapabilityModule> capabilityModules;
    private final CommandParser commandParser;
    private final PermissionGate permissionGate;

    public CommandRouterService(
            List<CapabilityModule> capabilityModules,
            CommandParser commandParser,
            PermissionGate permissionGate) {
        this.capabilityModules = capabilityModules;
        this.commandParser = commandParser;
        this.permissionGate = permissionGate;
    }

    public CommandResponse route(CommandRequest commandRequest) {
        CommandIntent commandIntent = commandParser.parse(commandRequest.command());
        AccessContext accessContext = new AccessContext(commandRequest.actorId(), commandRequest.permissions());

        permissionGate.assertAuthorized(commandIntent, accessContext);

        return capabilityModules.stream()
                .filter(module -> module.supports(commandIntent))
                .findFirst()
                .map(module -> module.handle(commandIntent, accessContext))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No capability module registered for " + commandIntent.capability()));
    }
}