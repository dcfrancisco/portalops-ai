# ADR-0019: Extensibility, channels and developer experience

Status: Proposed

## Context

The vision relies on modules contributing agents, skills, tools, knowledge and workspace components, and on reaching people where they work. No SDK, packaging, certification or notification model exists. Without them the platform cannot grow beyond first-party bundles safely.

## Decision

- **Extension SDK:** a published `portalops-sdk` with capability and tool annotations, schema generation, a local test harness (fake adapter, simulated kernel, ADR-0014), decision-test runner, project templates (Gradle workspace) and documentation. Extensions are OSGi bundles or, where possible, out-of-process tools over MCP (ADR-0012).
- **Packaging and trust:** extension manifest (capabilities, requested permissions, data classes, effects, risk tiers, dependencies, compatible versions), signature, SBOM, and a review gate before enabling. Third-party extensions run under least privilege with explicit grants from a tenant admin, and cannot register tools of tier 3 without certification.
- **Catalog:** an internal catalog of installed capabilities (generated from the registry) and, later, a marketplace for certified extensions and decision-table packs (for example a GDPR pack or an accessibility pack).
- **Notification and conversation channels:** pluggable channels for notifications, approvals and conversational access (in-portal inbox, email, Microsoft Teams, Slack, mobile push, ITSM tickets). Approvals via a channel link back to an authenticated step-up approval in the portal (ADR-0018). Channels are tools with the same policy, audit and redaction rules.
- **Workspace extensibility:** workspace components (cards, tables, trees, charts, forms) are contributed through a typed component contract rendered by the workspace engine. AI contributes data and layout intent, not arbitrary UI code.
- **Playbooks as code:** versioned playbooks (plans plus decision references) in a repository, reviewed and promoted through environments, with import and export.
- **Integrations:** event and webhook interfaces to CI/CD, ITSM, SIEM and observability tools. REST and GraphQL APIs mirror the capability registry.
- **Documentation and onboarding:** generated capability reference, runbooks, an admin guide, and guided setup in the workspace.

## Consequences

- Positive: ecosystem growth, faster domain coverage, integration with existing operations tooling.
- Negative: API stability commitments, an extension security review burden, and a support surface.
- Negative: marketplace and certification are a business and legal undertaking. They are deferred behind the SDK.
