package com.portalops.ai.api;

public interface PortalOpsAiProvider {

    public PortalOpsAnalysisResponse analyze(
            PortalOpsAnalysisRequest portalOpsAnalysisRequest);

    public String getProviderName();

}
