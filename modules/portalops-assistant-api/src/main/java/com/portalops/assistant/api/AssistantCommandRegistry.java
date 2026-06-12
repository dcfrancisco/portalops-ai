package com.portalops.assistant.api;

public interface AssistantCommandRegistry {

    public AssistantCommandHandler getAssistantCommandHandler(
            AssistantCommand assistantCommand);

}
