# ADR-0021: Agent and skill lifecycle management

Status: Proposed

## Context

Administrators need to add, change, disable and remove agents and skills to fit their portal without a code release. The Agent OS itself can see where capability is missing (unresolved intents, repeated manual steps, failing skills) and should be able to propose new agents and skills. Unrestricted self-extension would break the safety model: an agent that can create and activate its own capabilities can escalate its own privileges.

## Decision

### Who can do what

| Actor | Allowed |
| --- | --- |
| Tenant administrator (role: capability author) | Create, edit and submit declarative agents, skills and playbooks. Enable and disable per tenant. |
| Approver (a different person, role: capability approver) | Approve activation. Authors cannot approve their own changes. |
| Developer | Add coded skills and tools as signed extensions (ADR-0019). |
| **Agent OS (Agent Architect)** | Create **drafts and proposals only**: new skills, agents, playbooks, tests, and retirement suggestions. It cannot validate, approve, activate, or edit active definitions. |
| External agents | None. |

### Creation paths

1. **Studio (admin UI and API):** guided authoring with templates, a tool and skill picker, schema generation, a test runner and a simulation preview.
2. **Natural language to draft:** an administrator describes the need, the Agent Architect produces a draft manifest, tests and impact analysis, and the administrator reviews and edits.
3. **Gap-driven proposals:** the Agent Architect watches unresolved intents, repeated manual sequences, low-confidence plans and failing skills, and files proposals (with evidence from episodic memory) into a review queue.
4. **As code:** manifests are plain files, stored in a repository, reviewed and promoted through environments (playbooks as code, ADR-0019).

### Lifecycle states

```text
DRAFT -> VALIDATED -> SIMULATED -> APPROVED -> CANARY -> ACTIVE
                                                    \-> DEPRECATED -> RETIRED
ACTIVE/CANARY <-> DISABLED (kill switch, reversible)
```

- **VALIDATED:** schema, dependency and permission checks pass. Tools exist. Risk tier and effect are computed from the tools and cannot be lowered. Decision-table references resolve (ADR-0011). Budgets are set.
- **SIMULATED:** the skill's tests run in the twin (ADR-0014). Evaluation thresholds must pass with zero policy violations (ADR-0013).
- **APPROVED:** a different person approves. Tier 2 and 3 skills and any agent with write skills need four-eyes. The decision model sets the approver set.
- **CANARY:** activation for a subset (a site, a role group, a percentage of runs) with enhanced monitoring, then promotion to ACTIVE.
- **DEPRECATED then RETIRED:** see removal below.

### Hard limits (privilege containment)

- **Composition only.** Declarative skills and playbooks use existing tools and skills. They cannot introduce new effects, new tools, new adapters or new permissions.
- **No privilege escalation.** An agent or skill cannot hold permissions, a risk tier, an autonomy level or a budget above its creator's own authority and the tenant's policy ceiling. A tenant administrator cannot raise a level beyond the ceiling in a built-in safety decision table (ADR-0011).
- **No self-modification.** No agent can edit or activate any definition, including its own. The Agent Architect cannot approve. Learned procedures follow the same flow (ADR-0013).
- **Bounded growth.** Per-tenant limits on the number of agents, skills, delegation depth and fan-out. Only retrieval-selected candidates reach the planner (ADR-0020).
- **Signed and tamper-evident.** Active definitions are versioned, signed on activation and stored in PostgreSQL (ADR-0006). The kernel loads them read-only.
- **Everything is audited:** create, edit, approve, activate, disable, remove and every proposal by the Agent Architect (ADR-0005).

### Removal and change

- **Disable:** immediate, reversible, per tenant or global. In-flight runs finish their current step and then stop or switch to a fallback.
- **Remove:** disable, then **drain** (wait for in-flight runs), then **dependency check** (playbooks, agents, decision tables and schedules that reference it block removal until updated), then DEPRECATED with a notice period, then RETIRED. Definitions, versions, tests and audit history are retained per retention policy and never silently deleted.
- **Change:** edits create a new version that goes through the same states. The previous version stays available for rollback. Contract-breaking changes need a major version, and dependents are re-validated.
- **Built-in agents:** system agents (Orchestrator, Critic, Registry Steward and the policy agents) can be configured but not removed, because the kernel depends on them. Domain and other agents can be disabled per tenant.
- **Emergency:** kill switches at skill, agent, tenant and global level (ADR-0016) bypass the states, are themselves audited, and require no approver.

### Registry

A `portalops-registry` bundle holds manifests, versions, states, ownership, dependency graph and tenant overrides. `portalops-studio` provides the authoring UI and API. The registry hot-loads changes without a portal restart. Coded skills remain OSGi services. They are visible to the registry and follow the same states for enablement.

### Observability and feedback

Each agent and skill reports usage, success rate, verification failures, cost (ADR-0015) and approval statistics. Unused or failing skills are flagged for retirement proposals. Quality results feed back to authors (ADR-0013).

## Consequences

- Positive: administrators can adapt PortalOps without developers, and the Agent OS can help close capability gaps, while the safety model holds because creation and activation are separated.
- Positive: safe removal with dependency checks, rollback and a retained history.
- Negative: governance overhead for each change, and a review queue that can grow. Mitigation: templates, automated validation, and low-risk read-only skills get a lighter approval route set by the decision model.
- Negative: more infrastructure (registry, studio, signing) and a larger test surface.
- Negative: natural-language draft quality varies, so human review remains mandatory.
