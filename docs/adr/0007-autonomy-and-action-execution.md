# ADR-0007: Autonomy levels and governed action execution

Status: Proposed

## Context

All current capabilities are read-only. Operating a portal means changing state: reassigning workflow tasks, deactivating accounts, expiring content, triggering reindex. These changes carry risk and need a consistent safety model.

## Decision

Adopt five autonomy levels, configured per capability, scope and environment: L0 observe, L1 recommend, L2 approve and act, L3 policy-bounded autonomy, L4 reserved (not planned). Writes default to L2. Production is capped at L2 until explicitly raised for specific low-risk, reversible action classes.

Action lifecycle: **propose, preview (dry run), approve, execute, verify, report**.

- A write tool yields an `ActionProposal` containing evidence, a dry-run diff, risk tier, blast radius (count and scope), and a compensation plan.
- Approvals are human acts recorded in audit. The approver cannot be the model. Tier 3 needs two approvers.
- Execution is idempotent and carries the run id. Bulk actions are batched with limits and a stop-on-failure threshold.
- After execution the verifier re-reads state and compares it with the expected outcome. Failure triggers compensation or escalation.
- A per-instance kill switch halts all non-interactive runs.
- Scheduled and event-driven runs execute under a scoped service principal, never an administrator's session.

## Consequences

- Positive: consistent, reviewable operations. Safe incremental autonomy.
- Positive: approvals and verification produce trust data used to justify raising autonomy later.
- Negative: more UI (approval inbox, run timeline) and more tool authoring (dry run, compensation).
- Negative: approval fatigue. Mitigation: group proposals, tune L3 classes from approval statistics.
