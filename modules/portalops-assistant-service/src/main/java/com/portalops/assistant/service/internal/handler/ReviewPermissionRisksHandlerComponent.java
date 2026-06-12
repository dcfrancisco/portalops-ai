package com.portalops.assistant.service.internal.handler;

import com.portalops.api.knowledge.PortalKnowledgeService;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.permissions.PermissionFinding;
import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandHandler;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.PermissionRiskItem;
import com.portalops.assistant.api.payload.PermissionRiskPayload;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssistantCommandHandler.class)
public class ReviewPermissionRisksHandlerComponent
        implements AssistantCommandHandler {

    @Override
    public PortalOpsAssistantResponse<PermissionRiskPayload> execute(
            PortalOpsAssistantRequest portalOpsAssistantRequest) {

        PortalKnowledgeSnapshot portalKnowledgeSnapshot =
                _portalKnowledgeService.getSnapshot(
                        portalOpsAssistantRequest.getContext());

        List<PermissionRiskItem> permissionRiskItems = new ArrayList<>();

        for (PermissionFinding permissionFinding :
                portalKnowledgeSnapshot.getPermissionKnowledge().
                        getRiskyPermissions()) {

            permissionRiskItems.add(
                    new PermissionRiskItem(
                            permissionFinding.getPrincipalName(),
                            permissionFinding.getResourceName(),
                            permissionFinding.getActionKey(),
                            permissionFinding.getRiskLevel()));
        }

        PermissionRiskPayload permissionRiskPayload = new PermissionRiskPayload(
                permissionRiskItems);

        return new PortalOpsAssistantResponse<>(
                permissionRiskItems.isEmpty() ? AssistantStatus.INFO :
                        AssistantStatus.WARNING,
                "Permission Risks",
                permissionRiskItems.isEmpty() ?
                        "No risky permissions were detected in the current snapshot." :
                        "PortalOps found permission assignments that should be reviewed.",
                List.of(
                        permissionRiskItems.size() +
                                " permission risk finding(s) are currently flagged."),
                List.of(
                        "Validate elevated publish and configuration privileges.",
                        "Review service accounts and broad role grants."),
                List.of(
                        new AssistantAction(
                                "Review Permission Risks",
                                AssistantCommand.REVIEW_PERMISSION_RISKS.name())),
                permissionRiskPayload);
    }

    @Override
    public AssistantCommand getCommand() {
        return AssistantCommand.REVIEW_PERMISSION_RISKS;
    }

    @Reference
    private PortalKnowledgeService _portalKnowledgeService;

}
