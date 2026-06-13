package com.portalops.content.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetExpiredContentSkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve expired content in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves expired content for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Show expired content.",
                "How much expired content do we have?",
                "Tell me about expired content.");
    }

    @Override
    public String getName() {
        return "GetExpiredContent";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetContentTool");
    }

}
