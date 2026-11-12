# PortalOps Vision

PortalOps is an AI-native operational intelligence platform for enterprise portals.

PortalOps begins with Liferay, but the architectural model is intended to extend to additional portal and content management platforms over time.

## Product Direction

PortalOps is moving beyond a dashboard with an assistant panel.

The assistant is the primary interaction model.

The dashboard is a supporting view.

Users express intent. PortalOps investigates, gathers evidence, applies governance, and presents operational findings, actions, and workspaces.

## Operating Model

```text
Intent -> Investigation -> Findings -> Workspace -> Actions
```

This model replaces the older mental model of prompt-to-text-response systems.

## Agent OS

PortalOps is expanding from operational intelligence into full portal management and operations. The platform acts as an operating system for agents: PortalOps is the brain (kernel, policy, memory, tools), and LLMs are replaceable reasoning engines.

```text
Intent -> Plan -> Governed Execution -> Findings -> Workspace -> Approved Actions -> Verification -> Memory
```

Autonomy is earned incrementally, from observe to recommend to approve-and-act, with governance and audit enforced in code. See [Agent OS design](architecture/agent-os.md) and the [ADRs](adr/README.md).

## Examples

Examples of PortalOps intent:

- Review workflow health
- Find users with excessive permissions
- Analyze search failures
- Review stale content
- Audit role assignments

PortalOps should determine the relevant investigation and return structured operational outcomes.

## Why PortalOps Exists

Traditional portal administration forces users to navigate multiple screens, reports, and configuration areas to assemble an answer.

PortalOps reduces that burden by treating administration as investigation rather than navigation-first exploration.

## PortalOps Principles

- Intent is the entry point.
- Investigations are the unit of work.
- Governance is authoritative.
- The kernel decides; the model proposes.
- Every effect is mediated, authorized and audited.
- Knowledge is portal-specific and reusable.
- Skills orchestrate tools.
- AI contributes reasoning, not UI generation.
- Workspace rendering is a first-class capability.

## Long-Term Goal

PortalOps becomes the operational intelligence layer for enterprise portals by combining:

- Portal Knowledge
- Governance
- Operational Data
- AI Reasoning
- Skills
- Tools
- Structured Outcomes
- Workspace Rendering
