package com.portalops.assistant.service.internal;

import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantCommandRegistry;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandRegistry.class)
public class AssistantCommandRegistryComponent
        implements AssistantCommandRegistry {

    @Override
    public AssistantCommandHandler getAssistantCommandHandler(
            AssistantCommand assistantCommand) {

        for (AssistantCommandHandler assistantCommandHandler :
                _assistantCommandHandlers) {

            if (assistantCommandHandler.getCommand() == assistantCommand) {
                return assistantCommandHandler;
            }
        }

        return null;
    }

    @Reference
    private Collection<AssistantCommandHandler> _assistantCommandHandlers;

}
