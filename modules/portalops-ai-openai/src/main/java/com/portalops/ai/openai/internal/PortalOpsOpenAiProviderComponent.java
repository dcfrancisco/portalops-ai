package com.portalops.ai.openai.internal;

import com.portalops.ai.api.ActionLink;
import com.portalops.ai.api.FindingCard;
import com.portalops.ai.api.PortalOpsAiProvider;
import com.portalops.ai.api.PortalOpsAnalysisRequest;
import com.portalops.ai.api.PortalOpsAnalysisResponse;
import com.portalops.ai.api.Recommendation;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = PortalOpsAiProvider.class)
public class PortalOpsOpenAiProviderComponent implements PortalOpsAiProvider {

    @Override
    public PortalOpsAnalysisResponse analyze(
            PortalOpsAnalysisRequest portalOpsAnalysisRequest) {

        return new PortalOpsAnalysisResponse(
                "OpenAI analysis provider is scaffolded but not yet configured for live analysis.",
                List.of(
                        new FindingCard(
                                "AI Provider Status", "Configured Later",
                                "info",
                                "PortalOps owns the response model while this provider remains an optional analysis implementation.")),
                List.of(
                        new Recommendation(
                                "Keep deterministic execution primary",
                                "Use AI for analysis enhancements only after deterministic PortalOps findings are established.")),
                List.of(
                        new ActionLink("Open Assistant", "assistant", "")));
    }

    @Override
    public String getProviderName() {
        return "openai";
    }

}
