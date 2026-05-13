package com.portalops.ai.application.policy;

import com.portalops.ai.domain.command.CapabilityId;
import com.portalops.ai.domain.command.CommandIntent;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionGate {

    private static final Map<CapabilityId, String> REQUIRED_PERMISSIONS = Map.of(
            CapabilityId.PORTAL_MANAGEMENT, "portal.view",
            CapabilityId.WORKFLOW, "workflow.read",
            CapabilityId.PERMISSIONS, "permissions.read",
            CapabilityId.CONTENT, "content.read");

    public void assertAuthorized(CommandIntent commandIntent, AccessContext accessContext) {
        String requiredPermission = REQUIRED_PERMISSIONS.get(commandIntent.capability());

        if (!accessContext.permissions().contains(requiredPermission)) {
            throw new IllegalStateException(
                    "Actor %s is missing permission %s for %s".formatted(
                            accessContext.actorId(),
                            requiredPermission,
                            commandIntent.capability()));
        }
    }
}