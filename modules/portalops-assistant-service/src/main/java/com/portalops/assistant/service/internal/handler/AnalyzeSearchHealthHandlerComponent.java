package com.portalops.assistant.service.internal.handler;

import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.SearchHealthPayload;
import com.portalops.assistant.api.payload.SearchIssue;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = AssistantCommandHandler.class)
public class AnalyzeSearchHealthHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<SearchHealthPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        SearchHealthPayload searchHealthPayload = new SearchHealthPayload(
                12480, 2, "2026-06-12 09:10",
                List.of(
                        new SearchIssue(
                                "Slow incremental indexing", "warning",
                                "Recent content updates are taking longer than expected to appear in search."),
                        new SearchIssue(
                                "Zero result trend", "info",
                                "Repeated no-result searches are concentrated around policy content.")));

        return new PortalOpsAssistantResponse<>(
                AssistantStatus.WARNING, "Search Health",
                "Search is operational, but a few indexing signals need attention.",
                List.of(
                        "Search service is responding normally.",
                        "2 indexing failures were detected in the most recent monitoring window."),
                List.of(
                        "Review indexing logs for recent failures.",
                        "Consider a scoped reindex for affected content types."),
                List.of(
                        new AssistantAction("Reindex Search",
                                AssistantCommand.ANALYZE_SEARCH_HEALTH.name())),
                searchHealthPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.ANALYZE_SEARCH_HEALTH;
    }

}
