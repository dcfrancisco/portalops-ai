# ADR-0011: Decision models (DMN) for deterministic orchestration

Status: Proposed

## Context

The kernel mixes judgment calls with rules that should be predictable and reviewable: which capability handles an intent, how risky an action is, whether it needs approval and from whom, which model or provider may see which data, whether a physical actuation is allowed. ADR-0003 and ADR-0005 leave these as code or prompts. Rules in prompts are non-deterministic and unauditable. Rules in Java are hard for portal administrators and compliance teams to read, change and test.

## Decision

Use an explicit **decision model** for the deterministic parts of orchestration. We adopt DMN (Decision Model and Notation: decision tables, FEEL expressions, decision requirements graphs) behind a `portalops-decision` bundle and a decision SPI.

**LLMs reason, decision models decide.** The model proposes plans and classifications. Decision tables turn those facts into binding outcomes. A model output can be an input to a decision (for example a proposed tool and its arguments), never its result.

Decisions modeled as DMN:

| Decision | Inputs (examples) | Outputs |
| --- | --- | --- |
| Capability routing (deterministic fast path and fallback) | intent type, domain tags, modality, requester role | candidate capabilities, plan template |
| Risk classification | tool effect, record count, scope (site, instance), data classification, environment | risk tier 0 to 3 |
| Approval requirement | risk tier, autonomy setting, requester role, environment, time window | ALLOW, REQUIRE_APPROVAL, DENY, approver group, number of approvers |
| Autonomy level | capability, environment, historical approval and verification rate | L0 to L3 |
| Model and provider routing | data classification, modality, tenant policy, cost budget | provider, model, local or external, redaction obligations |
| Redaction and exposure | classification, destination, modality | obligations (mask, drop, blur, hash) |
| Physical actuation gate | device, zone, content type, time, rate, kill-switch state | allow, deny, approval |
| Escalation and SLA | finding severity, age, owner availability | notify, assign, escalate |
| Remediation playbook selection | finding type, platform, environment | playbook id and parameters |

Not modeled as DMN: open-ended investigation, novel multi-step planning, natural-language summarization and anything needing unstructured judgment. Those stay with the planner and LLM (ADR-0003). Long-running multi-step approvals and compensation flows are handled by the kernel run model (ADR-0007). BPMN is not adopted now.

**Properties required of every decision:**

- Deterministic, side-effect free and fast (local evaluation, no network calls).
- Versioned, with an explicit hit policy per table (for example UNIQUE or FIRST) and a defined default. Unmatched input results in the safest outcome (deny or require approval), never allow.
- Tested: each decision ships with decision test cases (inputs and expected outputs), executed in CI and before activation.
- Audited: every evaluation records decision id, version, inputs (redacted per classification), matched rules and output, linked to the run id (ADR-0005).
- Changed under governance: edits are drafts, validated by the tests, reviewed and approved (four-eyes for risk and approval tables) before activation, with rollback to a previous version.
- Tenant-aware: a base model ships with PortalOps. Instances can override only allowed tables, within limits. Overrides can tighten but not relax built-in safety tables (physical actuation gate, destructive-action approval).
- Tamper resistance: models are stored in PostgreSQL (ADR-0006), signed on activation, and loaded read-only by the kernel. The LLM and tools cannot edit them.

**Engine:** embed a DMN engine behind the SPI so the engine is replaceable. Candidates are Drools (KIE DMN) and Camunda's DMN engine, both Apache 2.0. Selection needs an OSGi packaging and Liferay classloading spike. The SPI exposes `evaluate(decisionId, inputs) -> DecisionResult` and `validate(model)`. Authoring uses standard DMN files with a table editor in the admin UI (a standard DMN editor can be embedded later).

## Consequences

- Positive: predictable, explainable and testable governance. Compliance can read the tables. Administrators change policy without code or redeploy.
- Positive: lower cost and latency for common intents (no LLM call for deterministic routing).
- Positive: a clean boundary between model judgment and enforceable rules, which strengthens the injection defense (ADR-0002).
- Negative: a new engine dependency, authoring workflow and test discipline. DMN tables can sprawl without ownership and review.
- Negative: OSGi classloading risk with the engine, mitigated by the spike and the SPI.
- Negative: FEEL has a learning curve. Mitigation: templates for common tables and a decision test runner in the admin UI.
- Policy evaluation in ADR-0005 is implemented by this engine. `PortalOpsCommandType` allow-lists are replaced by decision tables.
