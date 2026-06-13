package com.portalops.content.internal.runtime;

import com.portalops.api.runtime.PortalOpsTool;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsTool.class)
public class GetContentToolComponent implements PortalOpsTool {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Company-scoped web content retrieval",
                "Expired content retrieval",
                "Pending content retrieval");
    }

    @Override
    public String getDescription() {
        return "Retrieves structured web content summaries from the current Liferay company.";
    }

    @Override
    public String getName() {
        return "GetContentTool";
    }

}
