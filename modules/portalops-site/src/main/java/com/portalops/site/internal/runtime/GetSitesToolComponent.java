package com.portalops.site.internal.runtime;

import com.portalops.api.runtime.PortalOpsTool;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsTool.class)
public class GetSitesToolComponent implements PortalOpsTool {

    @Override
    public List<String> getCapabilities() {
        return List.of(
                "Company-scoped site retrieval",
                "Site membership counting",
                "Site page count retrieval");
    }

    @Override
    public String getDescription() {
        return "Retrieves structured site summaries from the current Liferay company.";
    }

    @Override
    public String getName() {
        return "GetSitesTool";
    }

}
