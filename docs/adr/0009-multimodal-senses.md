# ADR-0009: Multimodal senses: the OS speaks and sees

Status: Proposed. Extended by ADR-0010 to physical devices.

## Context

PortalOps is text-only. Operators want to talk to the system, hear briefings and approvals, and show it screenshots, documents and live portal pages. Many portal problems are visual (broken layouts, missing media, accessibility, brand drift) and cannot be found through APIs. Adding voice and vision ad hoc to the assistant would bypass the kernel's policy and audit path.

## Decision

Treat voice and vision as kernel-level I/O devices behind provider SPIs, in the same style as the LLM SPI (ADR-0002).

- **Input normalization:** every modality becomes an `IntentEnvelope` (voice, text) or an `Observation` (image, document, page capture) with a confidence, a classification and a reference to a stored artifact. The planner, policy engine and audit log see one model.
- **Speak:** `portalops-voice` provides speech-to-text and text-to-speech. Speech is generated from structured outcomes. Spoken approval is never enough: approvals need an explicit UI confirmation or step-up authentication (ADR-0007). Audio is not stored by default.
- **See:** `portalops-perception` provides OCR, captioning, and layout and chart understanding. `portalops-render` captures portal pages with a sandboxed headless browser limited to allow-listed hosts, for visual and accessibility inspection. `portalops-artifact` stores raw artifacts with hash, classification and retention.
- **Untrusted by default:** text from OCR, captions and third-party speech is data, never instructions. Images and audio are redacted (PII) before any external provider call, and pixels leave the system only if policy allows.
- **Consent and audit:** captures and recordings are consented, scoped, time-boxed and audited with the run id.
- **Memory:** captions and OCR text from artifacts are embedded into pgvector (ADR-0006) so past visual evidence is retrievable.
- **Degradation:** missing providers disable the modality only. Text paths are unaffected.

## Consequences

- Positive: new, high-value capabilities (visual QA, accessibility audits, hands-free briefings) without weakening governance.
- Positive: provider and local-model independence for voice and vision.
- Negative: more infrastructure (browser runtime, object storage) and a larger attack surface, mitigated by sandboxing and allow-lists.
- Negative: cost and latency of multimodal calls. Mitigation: budgets per run and caching by artifact hash.
- Negative: privacy and regulatory handling of voice and screenshots needs retention policy and user-facing disclosure.
