# ADR-0002: LLM is a replaceable reasoning engine, never an executor

Status: Proposed

## Context

Today the LLM only summarizes agent output. Agentic operation requires the model to plan and choose tools, which raises risks: prompt injection from portal content, unauthorized actions, vendor lock-in and data leakage. Two overlapping provider APIs also exist (`AIProvider` in `portalops-llm-spi` and `PortalOpsAiProvider` in `portalops-ai-api`).

## Decision

1. The model produces **proposals** (plans, tool-call requests, summaries) as structured JSON. The kernel validates them against the capability registry and executes them. The model has no direct access to the portal, network or credentials.
2. The model sees only the capabilities and data the requester is permitted to use, after redaction.
3. Portal content passed to the model is untrusted. It is delimited, and no policy decision depends on model output.
4. One provider SPI replaces the two existing ones. It supports structured output, tool-call proposals, token and cost reporting, and streaming. OpenAI stays the first implementation. Local or self-hosted models are first-class for restricted environments.
5. If no provider is available, deterministic L0 skills continue to work.

## Consequences

- Positive: switching or mixing providers is configuration. Injection cannot escalate privileges by itself.
- Positive: graceful degradation without an LLM.
- Negative: a stricter structured-output contract and more kernel validation code.
- Negative: consolidating the SPIs is a breaking change for `portalops-ai-openai`.
