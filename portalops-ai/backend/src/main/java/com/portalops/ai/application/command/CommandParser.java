package com.portalops.ai.application.command;

import com.portalops.ai.domain.command.CapabilityId;
import com.portalops.ai.domain.command.CommandIntent;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CommandParser {

    public CommandIntent parse(String rawCommand) {
        String normalized = rawCommand == null ? "" : rawCommand.trim().toLowerCase();
        String command = normalized.startsWith("/") ? normalized.substring(1) : normalized;
        String[] tokens = Arrays.stream(command.split("\\s+"))
                .filter(token -> !token.isBlank())
                .toArray(String[]::new);

        String operation = tokens.length == 0 ? "show" : tokens[0];
        String qualifier = tokens.length <= 1 ? "overview"
                : String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));

        return new CommandIntent(rawCommand, operation, resolveCapability(command), qualifier);
    }

    private CapabilityId resolveCapability(String command) {
        if (command.contains("workflow") || command.contains("approval")) {
            return CapabilityId.WORKFLOW;
        }

        if (command.contains("permission") || command.contains("role") || command.contains("publish")) {
            return CapabilityId.PERMISSIONS;
        }

        if (command.contains("content") || command.contains("page") || command.contains("asset")
                || command.contains("draft")) {
            return CapabilityId.CONTENT;
        }

        return CapabilityId.PORTAL_MANAGEMENT;
    }
}