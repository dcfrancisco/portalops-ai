# ADR-0016: Observability and operational resilience

Status: Proposed

## Context

An OS that operates other systems must itself be observable and dependable. Audit (ADR-0005) answers who did what. It does not give latency, error and saturation visibility, tracing across model calls and tools, or defined behavior under dependency failure. There is no stated availability, recovery or backpressure model.

## Decision

- **Telemetry:** OpenTelemetry traces, metrics and logs across kernel, tools, model calls, edge gateway and adapters. One trace per run, with spans per plan step, tool call, decision evaluation and model call. Attributes carry run id, tenant, capability, decision versions and cost. Sensitive payloads are not in telemetry by default.
- **Run replay and debug:** the run store (ADR-0001) plus traces allow step-by-step replay and "why" explanations of plans and decisions. A run viewer in the workspace shows the timeline, evidence and policy outcomes.
- **SLOs:** defined for interactive latency, tool success, approval-to-execution time, scheduled-run timeliness, retrieval quality and edge heartbeat freshness. Alerts and error budgets are in the health domain, and PortalOps monitors itself with its own capabilities.
- **Resilience patterns:** timeouts, retries with jitter, circuit breakers and bulkheads per provider, adapter and device. Idempotency keys for all writes. Queue-based, durable execution for scheduled and event runs, with backpressure and priority classes (interactive over batch, safety over everything).
- **Degradation ladder:** full agentic, then deterministic skills only (no LLM), then read-only, then stopped with a clear status. Each level is explicit and visible to users. Provider failover follows the decision model (ADR-0011).
- **Backup and disaster recovery:** PostgreSQL (ADR-0006) PITR and tested restore, with RPO and RTO targets, artifact store replication, and an exportable configuration (decision tables, policies, capabilities). Edge gateways operate offline and resync.
- **Capacity:** documented sizing for runs, embedding throughput, vector index size and telemetry volume, plus load and soak tests in CI nightly.
- **Kill switches:** per tenant, per capability, per zone and global, tested regularly.

## Consequences

- Positive: operability, faster incident response, defensible availability claims.
- Negative: instrumentation and testing effort, and telemetry volume and cost (ADR-0015).
- Negative: durable execution adds queue infrastructure (PostgreSQL-backed queue first, a broker only if scale requires).
