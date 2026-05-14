---
description: PortalOps AI product architecture and implementation rules
globs:
  - modules/portalops-*
  - client-extensions/portalops-*
alwaysApply: true
---

# PortalOps AI Rules

## Product Identity
PortalOps AI is an enterprise portal operations platform for Liferay 7.4.

This is NOT:
- a generic AI chatbot
- an FAQ assistant
- a demo playground

This IS:
- portal operations intelligence
- governance tooling
- workflow visibility
- permissions analysis
- content governance
- operational dashboards
- controlled portal actions

---

## Product Scope
PortalOps AI focuses on portal management.

Primary domains:
- workflow management
- permissions governance
- content governance
- site intelligence
- search diagnostics
- portal administration
- compliance insights

Example requests:
- Show pending workflows
- Show stale content
- Show risky permissions
- Show who can publish homepage
- Show orphaned pages
- Export governance reports

Future actions:
- Reassign stalled workflows
- Archive stale drafts
- Trigger governance scans
- Create approval tasks

---

## Architecture
PortalOps AI is a Liferay-native modular product.

Target platform:
Liferay 7.4

Use standard Liferay workspace modular architecture.

Preferred module types:
- API modules
- Service modules
- Web modules
- MVC Portlets
- REST modules
- Client Extensions when appropriate

Do NOT:
- fork Liferay
- modify Liferay core
- depend on unsupported internal implementations

Use supported Liferay APIs and service abstractions.

---

## Modular Design
PortalOps AI must be modular.

Example modules:

Core:
- portalops-api
- portalops-service
- portalops-web
- portalops-command
- portalops-audit
- portalops-policy
- portalops-config

Capabilities:
- portalops-workflow
- portalops-permissions
- portalops-content
- portalops-site
- portalops-search
- portalops-compliance
- portalops-admin

AI:
- portalops-llm-spi
- portalops-llm-openai
- portalops-llm-azure
- portalops-llm-bedrock

Optional:
- portalops-vector-spi
- portalops-vector-pgvector
- portalops-vector-pinecone

Modules must have clear responsibilities.

Avoid monolithic design.

---

## AI Integration
AI must integrate within Liferay modular architecture.

Preferred approach:
- OSGi services
- provider abstraction
- service interfaces
- adapter pattern

Avoid introducing separate backend platforms in MVP unless clearly required.

AI is an integrated capability, not a separate product.

---

## AI Providers
LLM providers must be pluggable.

Examples:
- OpenAI
- Azure OpenAI
- Anthropic
- AWS Bedrock

Never hardcode provider-specific logic into business modules.

---

## Vector Stores
Vector storage is optional.

Examples:
- pgvector
- Pinecone
- OpenSearch
- Azure AI Search

RAG is optional and not required for MVP.

---

## Interaction Model
PortalOps AI is NOT chat-first.

Supported interactions:
- slash commands
- natural language requests
- dashboards
- reports
- approval workflows
- operational consoles

Examples:
- /show workflows pending
- /show permissions risky
- /show stale content
- /show site anomalies

Use command routing and domain handlers.

---

## MVP Scope
Initial MVP is READ ONLY.

Allowed:
- workflow inspection
- permissions inspection
- content analysis
- reporting
- summaries
- recommendations

Disallowed:
- deleting content
- modifying permissions
- bulk publishing
- destructive automation

Write actions may be added only after approval workflows exist.

---

## Security
Enterprise-first requirements:
- RBAC awareness
- audit logging
- approval controls
- traceability
- no hidden side effects

---

## AI Usage
Use AI only where reasoning adds value:
- summarization
- explanation
- recommendations
- classification

Do NOT use AI for deterministic retrieval.

Example:
Retrieve workflows using Liferay APIs, then use AI to explain findings.

---

## Code Design
Prefer:
- interface-driven design
- modular OSGi architecture
- service abstraction
- adapter pattern
- testable services
- separation of concerns

Avoid:
- giant controllers
- business logic in UI modules
- tightly coupled provider implementations
- unsupported shortcuts
- direct provider API sprawl

---

## Output Expectations
Generated code must be:
- production-oriented
- modular
- maintainable
- enterprise-ready
- extensible
- compatible with Liferay 7.4