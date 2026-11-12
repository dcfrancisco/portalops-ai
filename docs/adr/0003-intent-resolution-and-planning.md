# ADR-0003: Replace keyword routing with registry-based intent resolution and planning

Status: Proposed

## Context

`PortalOpsAssistantServiceComponent` routes with `String.contains` checks in a fixed order (for example any prompt containing "user" goes to the user agent). Prompts spanning domains ("which inactive users still own stale content?") cannot be served. Each agent adds a near-duplicate code block, and the service depends on every agent module. A separate command path (`portalops-command`, `PortalOpsCommandType`) is a second intent system.

## Decision

- An **intent resolver** ranks registered capabilities (from the capability registry) against the intent. Matching uses declared example prompts and descriptions, with embeddings and an LLM tiebreak. Keyword rules remain only as a deterministic fallback.
- A **planner** produces a multi-step plan (sequential or parallel) across agents and tools. Plans are JSON data, validated and stored with the run.
- A **bounded loop** (max steps, tokens, time) lets the planner react to tool results.
- Fixed commands become named, parameterized intents with deterministic plans. Deterministic routing and plan-template selection are decision tables (ADR-0011), evaluated before any LLM call.
- Dispatch uses the registry. No kernel code references a concrete agent.

## Consequences

- Positive: cross-domain investigations. Adding a domain is registering a bundle.
- Positive: plans are explainable and auditable.
- Negative: non-deterministic planning needs evaluation. We need a regression suite of intents and expected plans.
- Negative: higher latency and cost per request. Mitigation: deterministic plans for common intents, caching.
