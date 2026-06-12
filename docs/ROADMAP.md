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

## Long-Term Model

PortalOps continues to follow this architecture:

```text
Intent -> Investigation -> Findings -> Workspace -> Actions
```
