# ADR-0013: Evaluation, feedback and continuous improvement

Status: Proposed

## Context

Non-deterministic planning (ADR-0003) and write actions (ADR-0007) are only safe if quality is measured. Today there are no tests for assistant behavior, no way to compare models or prompts, and no mechanism to learn from approvals, rejections and corrections.

## Decision

- **Evaluation harness** (`portalops-eval`): a versioned suite of scenarios (intent, fixture portal state, expected plan properties, expected findings, forbidden actions). It runs in CI and nightly against the simulation environment (ADR-0014). Metrics: task success, plan validity, tool-call accuracy, groundedness and citation accuracy, policy violation count (must be zero), hallucinated-capability rate, latency, cost (ADR-0015).
- **Gates:** a change to a prompt, model, provider, plan template or decision table cannot activate if the suite regresses past thresholds or shows any safety violation. Provider or model upgrades go through the same gate, with canary rollout.
- **Red teaming:** a maintained adversarial suite (prompt injection in portal content, OCR and speech, permission escalation, exfiltration, tool misuse, physical-actuation abuse) run on every release.
- **Feedback capture:** approvals, rejections, edits to proposals, user thumbs and verification failures are stored as episodic memory (ADR-0006) with the run id.
- **Learning loop:** feedback feeds (1) retrieval and ranking tuning, (2) suggested decision-table changes and playbooks as proposals for human review, (3) prompt and plan-template candidates evaluated by the harness. No self-modification of policy, code or tools. Learned artifacts are versioned and human-approved before use (procedural memory).
- **Autonomy evidence:** approval rate, verification success and incident counts per capability are the data that justify raising autonomy in the decision model.
- **Production monitoring:** sampled runs are scored offline by automated judges and human reviewers. Drift alerts feed the health domain.

## Consequences

- Positive: measurable quality, safe model and prompt changes, evidence for autonomy decisions.
- Negative: building and maintaining scenarios is real, ongoing work. LLM judges are imperfect, so human review of samples stays.
- Negative: storing run data for learning needs consent, redaction and retention rules (ADR-0017).
