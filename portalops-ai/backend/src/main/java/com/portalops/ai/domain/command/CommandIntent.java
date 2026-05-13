package com.portalops.ai.domain.command;

public record CommandIntent(
        String rawCommand,
        String operation,
        CapabilityId capability,
        String qualifier) {
}