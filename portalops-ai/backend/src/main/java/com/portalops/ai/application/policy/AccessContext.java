package com.portalops.ai.application.policy;

import java.util.Set;

public record AccessContext(String actorId, Set<String> permissions) {
}