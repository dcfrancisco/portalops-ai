package com.portalops.assistant.service.internal.handler;

import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.RecentChangeItem;
import com.portalops.assistant.api.payload.RecentChangesPayload;

import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service = AssistantCommandHandler.class)
public class ShowRecentChangesHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<RecentChangesPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        RecentChangesPayload recentChangesPayload = new RecentChangesPayload(
                List.of(
                        new RecentChangeItem(
                                "Configuration",
                                "Search tuning synonyms were updated for the Marketing site.",
                                "2026-06-12 08:15"),
                        new RecentChangeItem(
                                "Deployment",
                                "PortalOps web bundle was deployed to production.",
                                "2026-06-11 17:42"),
                        new RecentChangeItem(
                                "Permissions",
                                "A site role gained publish access on a content structure.",
                                "2026-06-11 13:05")));

        return new PortalOpsAssistantResponse<>(
                AssistantStatus.INFO, "Recent Changes",
                "PortalOps compiled a recent operational change summary.",
                List.of(
                        "Configuration, deployment, and permission changes were detected recently."),
                List.of(
                        "Review changes that correlate with active incidents.",
                        "Confirm whether recent permission updates were intentional."),
                List.of(
                        new AssistantAction(
                                "Show Recent Changes",
                                AssistantCommand.SHOW_RECENT_CHANGES.name())),
                recentChangesPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.SHOW_RECENT_CHANGES;
    }

}
