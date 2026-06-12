package com.portalops.ai.api;

import com.portalops.api.service.PortalOpsRequestContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PortalOpsAnalysisRequest implements Serializable {

    public PortalOpsAnalysisRequest(
            String prompt, Map<String, String> parameters,
            PortalOpsRequestContext context) {

        _context = Objects.requireNonNull(context);
        _parameters = Collections.unmodifiableMap(new HashMap<>(parameters));
        _prompt = Objects.requireNonNull(prompt);
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

    private final PortalOpsRequestContext _context;
    private final Map<String, String> _parameters;
    private final String _prompt;

}
