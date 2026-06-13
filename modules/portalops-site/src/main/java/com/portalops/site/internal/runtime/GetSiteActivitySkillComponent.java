package com.portalops.site.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetSiteActivitySkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve site activity metrics in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves site activity metrics for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Show site activity.",
                "Which sites look most active?",
                "Tell me about site activity.");
    }

    @Override
    public String getName() {
        return "GetSiteActivity";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetSitesTool");
    }

}
