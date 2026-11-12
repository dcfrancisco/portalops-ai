# ADR-0004: Unified capability and tool contract

Status: Proposed

## Context

`portalops-api` has metadata-only `PortalOpsAgent`, `PortalOpsSkill` and `PortalOpsTool` interfaces. Each `agent-*` module re-declares its own `AgentRequest`, `AgentResponse` and `Skill` types, and each agent has its own `execute(String prompt)` that resolves a skill by string. Tools declare no input schema, effect type, risk or permission requirements. The kernel cannot generically validate, authorize, audit or describe them to a model.

## Decision

Define one contract in `portalops-api`:

- `Capability`: a registered agent, skill or tool, with name, version, domain and example prompts.
- `ToolDescriptor`: JSON Schema input and output, `effect` (READ, WRITE, DESTRUCTIVE, EXTERNAL), `riskTier`, required Liferay permissions, idempotency, dry-run support, optional compensation tool, data classification.
- `ToolInvocation` and `ToolResult` with run id, step id, principal and mode (`DRY_RUN` or `EXECUTE`).
- A skill is a declarative or coded composition of tools. An agent is a named bundle of skills with a domain scope.

Registration is by OSGi service. The registry is the single source for the planner, the management agent (list agents, skills, capabilities) and the capability matrix.

The per-module duplicate DTOs and `Skill` types are removed. Existing agents are wrapped by an adapter during migration, then ported one domain at a time.

Export packages follow OSGi semantic versioning. Breaking changes need a major version bump and an ADR note.

## Consequences

- Positive: uniform validation, authorization, audit, redaction and dry-run. The capability matrix can be generated.
- Positive: third parties can contribute domains.
- Negative: the port touches all five agent modules.
- Negative: JSON Schema authoring cost per tool. Mitigation: derive from annotated DTOs.
