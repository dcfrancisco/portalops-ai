package com.portalops.content.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetContentSummarySkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve content summaries in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves content summaries for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Tell me about the content in this portal.",
                "How much content do we have?",
                "Show content summary.");
    }

    @Override
    public String getName() {
        return "GetContentSummary";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetContentTool");
    }

}
