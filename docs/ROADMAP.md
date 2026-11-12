# PortalOps Roadmap

This roadmap reflects the current AI-native direction of PortalOps.

The platform is evolving through layered capabilities rather than a single one-time implementation.

## Phase 1: OpenAI Provider

Goal:

- Integrate a real AI provider into the PortalOps assistant flow

Focus areas:

- OpenAI provider implementation
- Prompt-to-analysis execution
- Structured response generation
- Assistant-driven response rendering

Expected outcome:

- PortalOps can submit a real assistant prompt to OpenAI and render the resulting structured response in the PortalOps experience

## Phase 2: Knowledge Layer

Goal:

- Establish the retrieval foundation for portal-aware analysis

Focus areas:

- PostgreSQL
- `pgvector`
- Embeddings
- Document ingestion

Expected outcome:

- PortalOps can ingest portal-specific knowledge and retrieve relevant context for investigations

## Phase 3: Liferay Tools

Goal:

- Connect PortalOps to real Liferay operational capabilities

Focus areas:

- User APIs
- Role APIs
- Organization APIs
- Workflow APIs
- Search APIs
- Content APIs

Expected outcome:

- PortalOps investigations can gather real evidence from Liferay instead of relying on mock or static inputs

## Phase 4: Skills

Goal:

- Introduce the orchestration layer between intent and tools

Focus areas:

- User Management Skill
- Role Governance Skill
- Content Governance Skill
- Workflow Analysis Skill
- Search Analysis Skill
- Audit Skill
- System Health Skill

Expected outcome:

- PortalOps can resolve intent into skill execution paths that coordinate tools and produce structured outcomes

## Phase 5: Governance

Goal:

- Make governance a first-class control layer over recommendations and actions

Focus areas:

- Security policies
- Governance rules
- Portal standards
- Organizational policies

Expected outcome:

- PortalOps recommendations and actions are evaluated against authoritative governance constraints

## Phase 6: Workspace Rendering

Goal:

- Mature the rendering engine for structured operational outcomes

Focus areas:

- Cards
- Tables
- Trees
- Forms
- Charts
- Findings
- Actions

Expected outcome:

- PortalOps can render AI-assisted structured outcomes into reusable operational workspaces rather than relying on text-only responses

## Agent OS Phases (proposed)

These phases extend Phases 1-6 and are tracked by the [ADRs](adr/README.md). Phases are ordered by dependency.

| Phase | Goal | Key ADRs | Outcome |
| --- | --- | --- | --- |
| 7. Contracts | Unified capability and tool contract, single provider SPI, capability registry | 0002, 0004 | Existing agents run behind the new contract with no behavior change. |
| 8. Kernel | Run model, intent resolver, planner, bounded loop, executor, run persistence | 0001, 0003 | Cross-domain read-only investigations replace keyword routing. |
| 9. Governance | Policy decision point built on DMN decision tables (`portalops-decision`), risk tiers, append-only audit, Liferay permission bridge | 0005, 0011 | Every tool call is authorized and audited, and policy is readable, versioned and tested. |
| 10. Memory | PostgreSQL and pgvector, ingestion, episodic memory | 0006 | Retrieval-backed investigations and run history. |
| 11. Actions | Proposals, dry run, approval inbox, execution, verification, compensation | 0007 | First write capabilities at L2 (workflow, content, users). |
| 12. Operations | Scheduled and event-driven runs, service principals, kill switch, L3 policy classes | 0007 | Proactive operations within policy limits. |
| 13. Adapters | Liferay adapter extraction, platform-neutral domain models | 0008 | Foundation for AEM and other platforms. |
| 14a. Voice | Speech-to-text, text-to-speech, spoken briefings, voice never approves alone | 0009, 0007 | Hands-free intent and spoken findings. |
| 14b. Vision | Image and document understanding, page capture and visual and accessibility audits | 0009 | The OS can see screenshots, documents and live portal pages. |
| 14c. Physical | Edge gateway, device registry, physical audio and video, sensors, kiosks and signage, narrow gated actuation | 0010, 0009, 0007 | The OS can hear, see and speak in physical spaces, with privacy and safety controls. |
| 15. Quality | Evaluation harness, red-team suite, regression gates, feedback capture (starts with Phase 7 and gates every later phase) | 0013 | Measured, safe changes to prompts, models and decision tables. |
| 16. Simulation | State twin, scenario replay, impact reports in approvals, virtual devices | 0014 | Rehearsed actions and incidents. |
| 17. Interop | MCP server and client, A2A, multi-agent roles | 0012 | PortalOps integrates with the wider agent ecosystem. |
| 18. FinOps | Metering, budgets, cost-aware routing, carbon reporting (metering starts with Phase 8) | 0015 | Predictable cost and visible impact. |
| 19. Reliability and compliance | Telemetry, SLOs, DR, data governance and erasure, AI register, evidence packs | 0016, 0017 | Operable and auditable for regulated customers. |
| 20. Platform | Identity model, tenancy, Kubernetes topology, SDK, channels, signed extensions | 0018, 0019 | A platform others can build on. |
| 21. Catalog and studio | Registry with lifecycle states, studio authoring UI, Agent Architect drafts, catalog delivered by release (registry starts with Phase 7) | 0020, 0021 | Admins and the Agent OS can add and remove agents and skills safely. |

The [release slicing](architecture/agent-os.md#release-slicing-proposal) in the design maps these phases to releases R1 to R5. Phases 15 and 18 are cross-cutting and start early.
| 14. Workspace | Workspace renderer, approval inbox and run timeline in `portalops-web`, MCP and REST shells | 0001 | Dashboard mock replaced by real workspaces. |

Phase 2 uses PostgreSQL and pgvector, consistent with ADR-0006.

## Long-Term Model

PortalOps continues to follow this architecture:

```text
Intent -> Investigation -> Findings -> Workspace -> Actions
```
