# ADR-0015: Cost, carbon and resource budgets

Status: Proposed

## Context

Agentic loops, multimodal calls (ADR-0009) and always-on sensing (ADR-0010) can consume large and unpredictable amounts of tokens, compute and energy. Without budgets, cost and environmental impact are unmanaged and one runaway run can exhaust quotas.

## Decision

- **Metering:** every model call, embedding, STT and TTS call, vision call, render capture and edge upload is metered (tokens, seconds, bytes, currency, estimated energy and carbon intensity from provider data or configured factors) and attached to the run, tenant, user and capability.
- **Budgets:** hierarchical budgets (instance, team, capability, run) with soft warnings and hard stops. The kernel enforces step, token, time and currency limits (ADR-0003). Exceeding a hard limit halts the run and reports partial results.
- **Cost-aware routing:** the decision model (ADR-0011) selects the cheapest provider and model that meets quality and data-classification needs, using small or local models for classification, routing and summarization, and larger models for planning. Results are cached by content hash where safe.
- **Efficiency by design:** deterministic paths first (decision tables, templates), retrieval before generation, incremental ingestion, edge preprocessing, and sampled rather than continuous perception.
- **Reporting:** a usage and cost dashboard per tenant and capability, with forecast and anomaly alerts, exportable for chargeback. A carbon estimate is shown alongside cost.
- **Policy:** budgets and green preferences (for example prefer low-carbon regions or local inference) are decision-model inputs, with defaults per environment.

## Consequences

- Positive: predictable spend, runaway protection, visibility into environmental impact, evidence for model choices.
- Negative: metering overhead and approximate carbon figures. Reports label estimates as such.
- Negative: aggressive budgeting can reduce quality. Mitigation: the evaluation harness checks quality at each routing tier (ADR-0013).
