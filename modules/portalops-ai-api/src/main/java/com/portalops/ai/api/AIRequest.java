package com.portalops.ai.api;

import com.portalops.api.service.PortalOpsRequestContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AIRequest implements Serializable {

    public AIRequest(
            String prompt, Map<String, String> parameters,
            PortalOpsRequestContext context, String runtimeContext,
            String systemPrompt) {

        _context = Objects.requireNonNull(context);
        _parameters = Collections.unmodifiableMap(new HashMap<>(parameters));
        _prompt = Objects.requireNonNull(prompt);
        _runtimeContext = Objects.requireNonNull(runtimeContext);
        _systemPrompt = Objects.requireNonNull(systemPrompt);
    }

    public PortalOpsRequestContext getContext() {
        return _context;
    }

    public Map<String, String> getParameters() {
        return _parameters;
    }

    public String getPrompt() {
        return _prompt;
    }

    public String getRuntimeContext() {
        return _runtimeContext;
    }

    public String getSystemPrompt() {
        return _systemPrompt;
    }

    private final PortalOpsRequestContext _context;
    private final Map<String, String> _parameters;
    private final String _prompt;
    private final String _runtimeContext;
    private final String _systemPrompt;

}
