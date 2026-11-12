# ADR-0001: Adopt an Agent OS kernel as the platform core

Status: Proposed

## Context

PortalOps began as an assistant with read-only agents. `PortalOpsAssistantServiceComponent` contains the orchestration: it picks an agent by keyword, runs it, then asks the LLM to summarize. Governance, audit and knowledge exist as stubs. To manage and operate portals (including write actions) we need a coherent runtime that owns execution, safety and memory, not a chat handler that grows if-branches.

## Decision

PortalOps is structured as an operating system for agents. A `portalops-kernel` OSGi bundle owns:

- the run (process) model and lifecycle,
- a scheduler for interactive, scheduled and event-driven runs,
- the syscall path (tool invocation) with policy and audit hooks,
- the memory service interface,
- the verification step after actions.

Domain behavior (agents, skills, tools) are userland modules that register with the kernel. LLMs are "CPUs" behind an SPI. The kernel never imports domain modules.

## Consequences

- Positive: one place enforces authorization, audit, budgets and approvals. New domains need no kernel change. Matches the existing OSGi service-registry style.
- Positive: runs are persisted, so they are inspectable, replayable and resumable.
- Negative: significant refactor of `portalops-assistant-service` and the `agent-*` modules.
- Negative: persistence of runs adds a data store and migration concerns.
- Migration: keep current agents working behind an adapter while they move to the new contract (ADR-0004).

See [Agent OS design](../architecture/agent-os.md).
