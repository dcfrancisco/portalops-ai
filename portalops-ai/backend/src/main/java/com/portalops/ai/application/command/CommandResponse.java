package com.portalops.ai.application.command;

import java.util.Map;

public record CommandResponse(
        String capability,
        String operation,
        String summary,
        Map<String, Object> payload) {
}