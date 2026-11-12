# ADR-0014: Simulation and digital twin

Status: Proposed

## Context

Dry-run previews (ADR-0007) show what a single tool intends to change. They do not show downstream effects, and there is no safe place to test plans, playbooks, models and decision tables, or to rehearse incidents. Physical and portal changes both need rehearsal.

## Decision

Provide a **simulation environment** and a **digital twin** at three levels:

1. **Tool dry run:** each write tool returns a computed diff (ADR-0004). Required.
2. **State twin:** a snapshot of relevant portal state (users, roles, sites, pages, content metadata, workflow, search config) loaded into an isolated twin through a platform adapter (ADR-0008). Plans execute against the twin using the same executor with a `SIMULATION` mode. Impact is reported (affected users, pages, workflows, blast radius) and attached to the approval request.
3. **Scenario simulation:** replay of recorded runs, synthetic incidents (stuck workflows, reindex failure, permission drift) and, for physical sites, a virtual device layer with synthetic sensor streams (ADR-0010). Used by the evaluation harness (ADR-0013), training, and game-day exercises.

Rules:

- Simulation mode cannot reach production adapters, devices or external systems. The mode is enforced by the executor and decision model, not by the plan.
- Twin data is classified and redacted (masked PII) before leaving production, with retention limits.
- Twins are an approximation. Reports state the fidelity and unsimulated side effects. Verification after real execution (ADR-0007) remains mandatory.
- Tier 2 and 3 actions require a simulation report in the approval request when a twin is available for the affected domain.

## Consequences

- Positive: safer approvals, a testbed for evaluation and incident rehearsal, and confidence for raising autonomy.
- Negative: building adapters that can run against a twin is costly, and twins drift from production. Start with identity, workflow and content.
- Negative: storage and masking effort for snapshots.
