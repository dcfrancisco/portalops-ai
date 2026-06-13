package com.portalops.content.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetPendingContentSkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve pending content in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves pending content for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Show pending content.",
                "How much pending content do we have?",
                "Tell me about drafts and pending content.");
    }

    @Override
    public String getName() {
        return "GetPendingContent";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetContentTool");
    }

}
