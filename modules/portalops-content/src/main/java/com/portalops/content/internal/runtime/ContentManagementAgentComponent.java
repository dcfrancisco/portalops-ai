package com.portalops.content.internal.runtime;

import com.portalops.api.runtime.PortalOpsAgent;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsAgent.class)
public class ContentManagementAgentComponent implements PortalOpsAgent {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Retrieve content summaries in the current portal instance",
                "Retrieve expired content in the current portal instance",
                "Retrieve pending content in the current portal instance");
    }

    @Override
    public String getDescription() {
        return "Content administration operations.";
    }

    @Override
    public String getName() {
        return "ContentManagementAgent";
    }

    @Override
    public List<String> getSupportedSkills() {
        return List.of(
                "GetContentSummary", "GetExpiredContent",
                "GetPendingContent");
    }

}
