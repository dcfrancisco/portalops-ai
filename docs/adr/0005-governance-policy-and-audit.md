# ADR-0005: Policy decision point, risk tiers and mandatory audit

Status: Proposed

## Context

Governance is described as authoritative in the vision, but `CommandAuthorizer` allows every command except `UNSUPPORTED`, and `AuditRecorder` is a stub. With write actions in scope, governance must be real and must not depend on prompts.

## Decision

1. `portalops-policy` becomes a policy decision point (PDP). Inputs: principal, capability, arguments, run context, environment, risk tier, data classification. Outputs: `ALLOW`, `DENY` or `REQUIRE_APPROVAL`, with obligations (redaction, rate limit, dry-run first, notify).
2. Authorization is layered: Liferay permission check for the principal, then PortalOps policy. Both must allow.
3. Risk tiers: 0 read-only, 1 reversible low impact, 2 reversible high impact, 3 destructive or irreversible. Tier 3 requires four-eyes approval.
4. Policies are versioned, declarative data (not model output), implemented as DMN decision models (ADR-0011). Governance rules from knowledge (security policies, standards) can add constraints, never relax them.
5. `portalops-audit` is append-only and tamper-evident (hash chain). Every policy decision, tool call, approval, model call (metadata and hash, content optional by setting) and action result is recorded with run id, principal and correlation id. Audit failure blocks execution of write actions (fail closed).
6. Audit is exposed as a queryable source for the Audit domain, and can forward to Liferay's audit framework or a SIEM.

## Consequences

- Positive: defensible, reviewable control over agent behavior. Compliance evidence.
- Negative: added latency per syscall. Policy evaluation must stay local and fast.
- Negative: policy authoring UX becomes a product surface.
- Existing `PortalOpsCommandType` allow-lists are replaced by capability-based policy.
