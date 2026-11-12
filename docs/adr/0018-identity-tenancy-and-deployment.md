# ADR-0018: Identity, tenancy and deployment topology

Status: Proposed

## Context

Earlier ADRs assume per-instance isolation, scoped service principals, step-up authentication and a separate edge deployable, but the identity model, tenancy boundaries and runtime topology are not defined. PortalOps now includes PostgreSQL, an edge gateway, a browser runtime and model endpoints beyond the Liferay OSGi runtime.

## Decision

- **Identity:** human users authenticate through Liferay (SSO, SAML, OIDC). PortalOps runs as that principal. **Step-up authentication** (re-authentication or MFA) is required for tier 2 and 3 approvals and for sensitive voice sessions. Non-human identities are first class: service principals for scheduled and event runs, device identities (certificates, ADR-0010), and external agent identities (ADR-0012), each with scoped, expiring, revocable grants and an owner.
- **Roles:** operator, approver, policy author, auditor, device admin, tenant admin. Separation of duties: authors of a decision table cannot approve it, requesters cannot approve their own actions.
- **Tenancy:** the tenant is the Liferay virtual instance (company). All stores carry `company_id` with row-level security (ADR-0006). Tenant-scoped config, budgets (ADR-0015), decision overrides (ADR-0011), providers and retention. A single-tenant dedicated deployment is supported for restricted customers.
- **Deployment topologies:**
  1. *Embedded:* OSGi bundles in Liferay plus PostgreSQL, for small installations and development (`compose.yaml`).
  2. *Standard:* Liferay cluster plus a PortalOps worker pool (runs, ingestion, perception) as containerized services on Kubernetes, with PostgreSQL HA, object storage and a secrets manager.
  3. *Edge:* standard plus edge gateways at sites (ADR-0010).
  Kernel logic is packaged so workers can run outside the portal JVM, talking to Liferay through the adapter (ADR-0008). Heavy or risky components (browser runtime, media processing, local models) run isolated from the portal.
- **Secrets and keys:** a secrets manager (not OSGi config files) for provider keys, database credentials and device certificates, with rotation. Customer-managed keys where supported.
- **Network:** outbound allow-lists for providers, a dedicated egress proxy with logging, private connectivity for model endpoints, segmentation of the render sandbox and edge networks.
- **Upgrades:** semantic versioning of capability contracts and decision models, backward compatible migrations (expand then contract), canary and rollback for kernel, models and edge fleet.

## Consequences

- Positive: a clear security and scaling story, safe multi-tenancy, and room to grow beyond the portal JVM.
- Negative: operating several runtimes (workers, edge, database) raises complexity versus a pure OSGi deployment. Mitigation: the embedded topology remains for small and development installs.
- Negative: splitting kernel execution from the portal JVM needs a stable remote contract and added authentication between them.
