package com.portalops.site.internal.runtime;

import com.portalops.api.runtime.PortalOpsSkill;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsSkill.class)
public class GetSiteMembershipSkillComponent implements PortalOpsSkill {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve site membership metrics in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Retrieves site membership metrics for the current portal instance.";
    }

    @Override
    public List<String> getExamplePrompts() {
        return List.of(
                "Show site membership.",
                "Which sites have the most members?",
                "Tell me about site memberships.");
    }

    @Override
    public String getName() {
        return "GetSiteMembership";
    }

    @Override
    public List<String> getSupportedTools() {
        return List.of("GetSitesTool");
    }

}
