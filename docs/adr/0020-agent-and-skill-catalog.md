# ADR-0020: Agent and skill catalog

Status: Proposed

## Context

Agents exist for five domains (user, site, search, content, management), each with a fixed set of coded skills and its own types (ADR-0004). The expanded platform needs agents for operations, governance, knowledge, perception, physical devices and integration, plus shared skills. Without an agreed taxonomy the catalog will grow ad hoc, with overlapping scopes, inconsistent risk levels and no clear ownership.

## Decision

Adopt a four-group catalog, defined in the [Agent and Skill Catalog](../architecture/agent-skill-catalog.md):

1. **System agents:** Orchestrator, Critic and Verifier, Governance Advisor, Knowledge Librarian, Memory Curator, Registry Steward (the existing management agent), Agent Architect, Quality Steward, Budget Steward, Briefing Agent.
2. **Domain agents:** Identity and Access, Site and Page, Content, Workflow, Search, Platform Health, Audit and Compliance, Security Posture, Performance, Localization and Accessibility. Existing agents map to these and keep their skills.
3. **Sensory and physical agents:** Voice, Vision, Visual QA, Facility and Edge, Signage and Announcement.
4. **Integration agents:** Notification, Ticketing, External Agent Gateway.

Rules:

- **One domain, one owning agent.** Overlap is resolved by delegation (child runs, ADR-0012), not by duplicating skills.
- **Read before write.** Each domain ships read skills first. Write skills are added separately with a default autonomy of L2 and explicit risk tiers (ADR-0007).
- **Policy envelope per agent:** a maximum risk tier, maximum autonomy, budget, allowed data classes and modalities. An agent can never run a skill above its envelope, whatever the plan says.
- **Skills are the unit of reuse.** Agents compose skills, skills compose tools (ADR-0004). Common skills (summarize, correlate, estimate impact, verify) are shared building blocks.
- **Skill kinds:** coded, declarative, playbook and external. Only coded skills can introduce new tools or effects. The others compose what exists.
- **Every skill ships with tests** (evaluation scenarios, ADR-0013) and declares simulation support (ADR-0014).
- **Naming and ownership:** names are unique per tenant and versioned semantically. Every agent and skill has a named owner and an escalation contact.
- **Discovery by retrieval:** the planner receives the top permitted candidates found by embedding search, not the full catalog, so the catalog can scale.
- **The catalog is generated from the registry.** The capability matrix and the Registry Steward read the same source, and the matrix is no longer hand-maintained.
- **Sequencing:** the catalog is delivered by release (R1 to R5) as listed in the catalog doc.

## Consequences

- Positive: a shared map for planning, ownership and release scoping. Fewer overlapping agents. Clear safety limits per agent.
- Positive: the catalog can grow without changing the kernel.
- Negative: an up-front taxonomy can be wrong. Mitigation: it is a starting point, changeable through ADR-0021.
- Negative: many agents means more evaluation scenarios and maintenance. Mitigation: shared skills and the release sequencing.
