# Operational Intelligence Philosophy

PortalOps is an AI-native Operational Intelligence Platform for Liferay.

PortalOps is designed to support operational decision making, not just answer questions.

## Intent Over Navigation

Traditional portal administration requires users to navigate multiple screens, reports, and configuration areas to piece together answers.

PortalOps introduces an intent-driven model where users express what they want to understand or accomplish, and PortalOps performs the relevant investigation.

Examples:

- Analyze search failures
- Review workflow health
- Find stale content
- Show users with excessive permissions
- Review inactive users

## What PortalOps Combines

PortalOps combines:

- Portal Knowledge
- Governance Rules
- Operational Data
- AI Reasoning
- Workspace Rendering
- Actions

Operational intelligence comes from how these capabilities work together, not from language generation alone.

## Investigation Mindset

Every assistant request should be treated as an operational investigation.

Examples of investigation types:

- Role Governance Investigation
- Content Governance Investigation
- Workflow Investigation
- Search Investigation
- Audit Investigation
- System Health Investigation

Each investigation should gather relevant context, apply governance constraints, and produce a structured outcome.

## Governance First

PortalOps recommendations and actions must remain aligned with:

- Security policies
- Portal governance rules
- Operational procedures
- Organizational standards

Governance remains authoritative over AI recommendations.

## Primary Questions

PortalOps should answer these questions first:

1. What did I find?
2. Why does it matter?
3. What should I do next?

Detailed narrative explanation comes later and only when it adds value.

## Operational Outcome Model

PortalOps should respond with structured outcomes such as:

1. Summary
2. Findings
3. Recommendations
4. Actions
5. Workspace components where needed

This reflects how operations engineers consume information:

- Identify the problem
- Understand the impact
- Move to action

## Workspace, Not Just Responses

PortalOps should render investigations into a workspace that may include:

- Cards
- Tables
- Trees
- Forms
- Charts
- Findings panels
- Action panels

The renderer owns the user experience. AI contributes analysis, not UI generation.

## Long-Term Direction

PortalOps is initially focused on Liferay, but the architectural direction is broader.

The long-term model is:

```text
Intent -> Investigation -> Findings -> Workspace -> Actions
```

That model can extend to additional portal and content platforms in the future without changing the operational philosophy.
