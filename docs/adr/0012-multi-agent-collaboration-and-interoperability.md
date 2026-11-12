# ADR-0012: Multi-agent collaboration and interoperability

Status: Proposed

## Context

The kernel plans across tools, but agents are still isolated domain bundles. Complex operations (a security review spanning identity, content and search) benefit from specialist agents that delegate, critique and negotiate. External ecosystems (Liferay's MCP server, enterprise agents, ITSM and CI/CD tools) also need to call PortalOps and be called by it.

## Decision

- **Delegation model:** the kernel remains the only orchestrator. Agents may request sub-runs from the kernel (child runs with their own budget, policy scope and audit trail) but never call each other directly. Delegation depth and fan-out are bounded.
- **Roles:** planner, specialist (domain), critic or verifier (reviews plans and results, can veto through the decision model, ADR-0011), and summarizer. A critic runs on a different provider or prompt than the planner where practical.
- **Shared state:** agents share a run blackboard (typed observations and findings), not raw conversation history. Everything on it is attributable, policy-filtered and audited.
- **Interoperability:**
  - **MCP server:** PortalOps capabilities are exposed as MCP tools, filtered by the caller's permissions and policy. Write tools follow the same approval flow.
  - **MCP client:** the kernel can consume external MCP tools as registered capabilities, with a descriptor, risk tier and allow-list like any other tool (ADR-0004). External tools are untrusted by default.
  - **Agent-to-agent protocol (A2A):** agent cards for discovery and task exchange with external agents, behind the same authentication, policy and audit. Adopt as the standard matures.
- **Trust boundary:** results from external agents are observations, not commands. They cannot change policy, plans' approval status or autonomy levels.

## Consequences

- Positive: specialist depth, independent review of risky plans, and integration with the wider tooling ecosystem.
- Negative: higher cost and latency, and harder debugging. Mitigation: depth and budget limits, run trace tree, and the evaluation harness (ADR-0013).
- Negative: protocol churn (MCP and A2A are evolving). Mitigation: protocol adapters behind the capability contract.
