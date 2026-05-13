package com.portalops.ai.application.command;

import com.portalops.ai.adapter.out.liferay.StubLiferayGateway;
import com.portalops.ai.application.policy.PermissionGate;
import com.portalops.ai.module.content.ContentModule;
import com.portalops.ai.module.permissions.PermissionsModule;
import com.portalops.ai.module.portal.PortalManagementModule;
import com.portalops.ai.module.workflow.WorkflowModule;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandRouterServiceTest {

    private final StubLiferayGateway liferayGateway = new StubLiferayGateway();
    private final CommandRouterService commandRouterService = new CommandRouterService(
            List.of(
                    new PortalManagementModule(liferayGateway),
                    new WorkflowModule(liferayGateway),
                    new PermissionsModule(liferayGateway),
                    new ContentModule(liferayGateway)),
            new CommandParser(),
            new PermissionGate());

    @Test
    void routesWorkflowCommandsToWorkflowModule() {
        CommandResponse response = commandRouterService.route(
                new CommandRequest(
                        "analyst",
                        Set.of("workflow.read"),
                        "/show workflows pending",
                        Map.of()));

        assertEquals("WORKFLOW", response.capability());
    }

    @Test
    void rejectsUnauthorizedCommands() {
        assertThrows(
                IllegalStateException.class,
                () -> commandRouterService.route(
                        new CommandRequest(
                                "analyst",
                                Set.of("workflow.read"),
                                "/show permissions risky",
                                Map.of())));
    }
}