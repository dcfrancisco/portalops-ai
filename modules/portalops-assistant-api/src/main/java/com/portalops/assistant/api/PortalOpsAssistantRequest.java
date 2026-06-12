package com.portalops.assistant.api;

import com.portalops.api.service.PortalOpsRequestContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PortalOpsAssistantRequest implements Serializable {

    public PortalOpsAssistantRequest(
            AssistantCommand command, Map<String, String> parameters,
            PortalOpsRequestContext context) {

        _command = Objects.requireNonNull(command);
        _context = Objects.requireNonNull(context);
        _parameters = Collections.unmodifiableMap(new HashMap<>(parameters));
    }

    public AssistantCommand getCommand() {
        return _command;
    }

    public PortalOpsRequestContext getContext() {
        return _context;
    }

    public Map<String, String> getParameters() {
        return _parameters;
    }

    private final AssistantCommand _command;
    private final PortalOpsRequestContext _context;
    private final Map<String, String> _parameters;

}
