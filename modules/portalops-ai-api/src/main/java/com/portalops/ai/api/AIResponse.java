package com.portalops.ai.api;

import java.io.Serializable;
import java.util.Objects;

public class AIResponse implements Serializable {

    public AIResponse(
            boolean success, String content, String providerType,
            String modelName, String errorMessage) {

        _content = Objects.requireNonNull(content);
        _errorMessage = Objects.requireNonNull(errorMessage);
        _modelName = Objects.requireNonNull(modelName);
        _providerType = Objects.requireNonNull(providerType);
        _success = success;
    }

    public String getContent() {
        return _content;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public String getModelName() {
        return _modelName;
    }

    public String getProviderType() {
        return _providerType;
    }

    public boolean isSuccess() {
        return _success;
    }

    private final String _content;
    private final String _errorMessage;
    private final String _modelName;
    private final String _providerType;
    private final boolean _success;

}
