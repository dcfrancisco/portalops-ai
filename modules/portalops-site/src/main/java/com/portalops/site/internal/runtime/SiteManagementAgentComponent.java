package com.portalops.site.internal.runtime;

import com.portalops.api.runtime.PortalOpsAgent;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsAgent.class)
public class SiteManagementAgentComponent implements PortalOpsAgent {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve sites in the current portal instance",
                "Retrieve site membership metrics in the current portal instance",
                "Retrieve site activity metrics in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Site administration operations.";
    }

    @Override
    public String getName() {
        return "SiteManagementAgent";
    }

    @Override
    public List<String> getSupportedSkills() {
        return List.of("GetSites", "GetSiteMembership", "GetSiteActivity");
    }

}
