package com.portalops.ai.adapter.in.web;

import com.portalops.ai.application.command.CommandRequest;
import com.portalops.ai.application.command.CommandResponse;
import com.portalops.ai.application.command.CommandRouterService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommandController {

    private final CommandRouterService commandRouterService;

    public CommandController(CommandRouterService commandRouterService) {
        this.commandRouterService = commandRouterService;
    }

    @GetMapping("/modules")
    public Map<String, List<String>> modules() {
        return Map.of(
                "core", List.of("command-routing", "policy-enforcement", "liferay-adapter"),
                "capabilities", List.of("portal-management", "workflow", "permissions", "content"));
    }

    @PostMapping("/commands")
    public CommandResponse execute(@Valid @RequestBody CommandRequest commandRequest) {
        return commandRouterService.route(commandRequest);
    }
}