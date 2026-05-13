package com.portalops.ai.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.Set;

public record CommandRequest(
        @NotBlank String actorId,
        @NotEmpty Set<String> permissions,
        @NotBlank String command,
        Map<String, Object> parameters) {
}