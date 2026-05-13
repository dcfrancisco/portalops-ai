# PortalOps AI Architecture Skeleton

PortalOps AI is split into two deployable surfaces:

- `client-extensions/portalops-shell`: Liferay 7.4 frontend shell for dashboards, command entry, and operational module navigation.
- `portalops-ai/backend`: Spring Boot and Spring AI backend for command routing, policy enforcement, module orchestration, and future reasoning workflows.

## Module Layout

Frontend responsibilities:

- render the PortalOps shell inside Liferay
- capture slash commands and guided actions
- present workflow, permissions, content, and portal management views
- call the backend through a stable HTTP contract

Backend responsibilities:

- parse natural language or slash commands into deterministic intents
- enforce module-level permissions before execution
- route requests to capability modules
- call Liferay through an adapter boundary
- add Spring AI only after deterministic retrieval is complete

## Backend Packages

- `application.command`: command parsing, routing, request and response contracts
- `application.policy`: access context and permission gate enforcement
- `domain.command`: routing primitives and capability identifiers
- `domain.workflow`: workflow read models
- `domain.permissions`: permission governance read models
- `domain.content`: content hygiene read models
- `module.portal`: portal management capability
- `module.workflow`: workflow capability
- `module.permissions`: permissions capability
- `module.content`: content capability
- `adapter.in.web`: REST ingress
- `adapter.out.liferay`: Liferay integration seam

## Request Flow

1. The Liferay shell submits a command to the backend.
2. `CommandRouterService` parses the raw request into a `CommandIntent`.
3. `PermissionGate` validates access against the targeted capability.
4. The selected capability module executes a read-only use case.
5. The Liferay adapter returns deterministic portal data.
6. Spring AI can later summarize or explain the result without owning retrieval.

## Validation

Run the backend unit tests with:

```bash
./gradlew -p portalops-ai/backend test
```

Deploy the Liferay client extension with:

```bash
blade gw deploy
```