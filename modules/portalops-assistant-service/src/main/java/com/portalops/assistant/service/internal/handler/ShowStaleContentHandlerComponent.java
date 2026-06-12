package com.portalops.assistant.service.internal.handler;

import com.portalops.api.content.ContentSummary;
import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.StaleContentItem;
import com.portalops.assistant.api.payload.StaleContentPayload;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandHandler.class)
public class ShowStaleContentHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<StaleContentPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        PortalKnowledgeSnapshot portalKnowledgeSnapshot =
                _portalKnowledgeService.getSnapshot(
                        portalOpsAssistantRequest.getContext());

        List<StaleContentItem> staleContentItems = new ArrayList<>();

        for (ContentSummary contentSummary :
                portalKnowledgeSnapshot.getContentKnowledge().getStaleContent()) {

            staleContentItems.add(
                    new StaleContentItem(
                            contentSummary.getTitle(), contentSummary.getType(),
                            contentSummary.getStatus()));
        }

        StaleContentPayload staleContentPayload = new StaleContentPayload(
                staleContentItems);

        return new PortalOpsAssistantResponse<>(
                staleContentItems.isEmpty() ? AssistantStatus.INFO :
                        AssistantStatus.WARNING,
                "Stale Content",
                staleContentItems.isEmpty() ?
                        "No stale content was found in the current snapshot." :
                        "PortalOps found content items that appear stale.",
                List.of(
                        staleContentItems.size() +
                                " stale content item(s) were returned for review."),
                List.of(
                        "Review content ownership for stale assets.",
                        "Retire or refresh outdated content with high visibility."),
                List.of(
                        new AssistantAction(
                                "Show Stale Content",
                                AssistantCommand.SHOW_STALE_CONTENT.name())),
                staleContentPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.SHOW_STALE_CONTENT;
    }

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

}
