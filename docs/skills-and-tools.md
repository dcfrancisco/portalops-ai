# PortalOps Skills and Tools

PortalOps introduces skills as the orchestration layer between user intent and operational tools.

Skills do not replace platform APIs. Skills coordinate them.

## Runtime Model

```text
Intent -> Skill -> Tools -> Structured Outcome -> Workspace -> Actions
```

## Skills

Examples of PortalOps skills:

- User Management Skill
- Role Governance Skill
- Content Governance Skill
- Workflow Analysis Skill
- Search Analysis Skill
- Audit Skill
- System Health Skill

Skills interpret an investigation context and decide which tools and evidence are needed.

## Tools

Examples of PortalOps tools:

- User APIs
- Role APIs
- Organization APIs
- Workflow APIs
- Search APIs
- Content APIs

Tools provide evidence and operational data to skills.

## Skill Responsibilities

Skills may:

- Gather evidence
- Correlate results
- Apply governance-aware logic
- Prepare structured findings
- Suggest actions

## Module Direction

In the long term, intelligence modules may contribute their own:

- Skills
- Tools
- Knowledge
- Governance Rules
- Workspace Components

This keeps PortalOps aligned with a modular Liferay OSGi architecture.
