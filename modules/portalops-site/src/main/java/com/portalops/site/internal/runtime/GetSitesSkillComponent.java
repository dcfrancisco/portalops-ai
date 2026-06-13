package com.portalops.site.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetSitesSkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of("Retrieve sites in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves site summaries for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Tell me about the sites in this portal.",
                "How many sites do we have?",
                "List sites in this portal.");
    }

    @Override
    public String getName() {
        return "GetSites";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetSitesTool");
    }

}
