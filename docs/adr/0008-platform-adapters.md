# ADR-0008: Platform adapter layer, Liferay first

Status: Proposed

## Context

The vision says PortalOps should extend to AEM, WordPress, Drupal and others. Today agents call Liferay services directly, and bundles such as `portalops-content`, `-site`, `-search`, `-workflow` and `-service` are empty placeholders. Platform specifics leak into domain logic.

## Decision

- Domain bundles (identity, site, content, workflow, search, health, audit) depend only on platform-neutral service interfaces in `portalops-api`.
- `portalops-adapter-liferay` implements those interfaces using Liferay services, and is the only bundle that imports Liferay service APIs for operations. Other platforms get their own adapters later.
- Domain models (user, site, page, content item, workflow task, finding) are platform-neutral, with platform extension attributes.
- Adapters declare which capabilities they support. The registry hides capabilities an adapter cannot back.
- Prefer Liferay-native integration points (including the Liferay MCP server where available) before direct service calls. Follow the repository's Liferay workspace rules for the target product version.
- Placeholder bundles are either implemented as adapter or domain code, or removed.

## Consequences

- Positive: a clean path to other portals. Easier testing with fake adapters.
- Negative: an extra abstraction layer, and risk of a lowest-common-denominator model. Mitigation: extension attributes and adapter-specific capabilities.
- Negative: porting current direct Liferay calls out of the agents.
