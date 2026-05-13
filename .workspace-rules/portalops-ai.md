# PortalOps AI – GitHub Copilot Instructions

## Product Vision
PortalOps AI is an enterprise portal operations platform for Liferay 7.4.

This is NOT a generic AI chatbot or FAQ assistant.

The platform provides operational intelligence, governance, analysis, and controlled actions for enterprise portal administration.

Primary focus:
- Workflow management
- Permissions governance
- Content governance
- Site intelligence
- Search diagnostics
- Portal administration
- Compliance and operational visibility

Users should be able to ASK the platform to show things and DO things safely.

Examples:
- Show pending workflows
- Show stale content
- Show risky permissions
- Show who can publish the homepage
- Show orphaned pages
- Export governance reports

Future examples:
- Reassign stalled workflows
- Archive stale drafts
- Trigger cleanup scans
- Create approval tasks

---

## Architecture Principles

### Modular Design
Design the system as modular capability packs.

Avoid monolithic design.

Example modules:
- portalops-core
- portalops-command
- portalops-workflow
- portalops-permissions
- portalops-content
- portalops-site
- portalops-search
- portalops-compliance
- portalops-admin
- portalops-liferay-adapter
- portalops-audit
- portalops-policy

Modules should have clear responsibilities.

---

### Liferay Integration
Target platform:
Liferay 7.4

Use supported extension patterns.

DO NOT modify or fork Liferay core.

Prefer:
- Headless APIs
- service abstractions
- client extensions
- remote app integration
- standard Liferay module development

Avoid direct coupling to internal implementation details where possible.

---

### AI Backend
AI capabilities should be implemented in a separate Spring Boot backend.

Use:
- Java 21
- Spring Boot
- Spring AI

Avoid embedding complex AI dependencies directly into Liferay OSGi modules unless required.

---

### AI Provider Abstraction
LLM providers must be pluggable.

Examples:
- OpenAI
- Azure OpenAI
- Anthropic
- AWS Bedrock

Do not hardcode provider-specific logic in business modules.

---

### Vector Store Abstraction
Vector storage should be optional and pluggable.

Examples:
- pgvector
- Pinecone
- OpenSearch
- Azure AI Search

RAG is optional, not mandatory for MVP.

---

## Interaction Model

This is NOT chatbot-first.

Supported interaction modes:
- structured commands
- natural language requests
- dashboards
- reports
- approval workflows

Command examples:
- /show workflows pending
- /show permissions risky
- /show stale content
- /show site anomalies

Commands should be parsed through a routing layer.

---

## MVP Scope
Initial MVP is READ-ONLY.

Allowed:
- inspect workflows
- inspect permissions
- inspect content health
- generate reports
- summarize findings

NOT in MVP:
- delete content
- modify permissions
- bulk publish
- destructive actions

Safe actions may be added later behind approvals.

---

## Security Principles
Enterprise-first design.

Requirements:
- RBAC awareness
- audit logging
- approval workflows for sensitive actions
- no destructive actions without safeguards
- traceability for AI-driven decisions

---

## Code Design
Prefer:
- interface-driven design
- clean architecture
- dependency inversion
- modular services
- adapter pattern
- provider abstractions

Avoid:
- tightly coupled service classes
- giant controllers
- provider-specific business logic
- hardcoded credentials
- direct API calls scattered across modules

---

## LLM Usage
Use AI where reasoning adds value:
- summarization
- explanation
- recommendations
- classification

Do NOT use AI for deterministic data retrieval if backend logic is sufficient.

Example:
Fetch workflow data deterministically, then use AI to explain findings.

---

## Output Expectations
Generated code should be:
- production-oriented
- modular
- enterprise maintainable
- testable
- extensible

Avoid demo-only shortcuts unless explicitly requested.

---
description: PortalOps AI product architecture and implementation rules
globs:
  - modules/portalops-*
  - client-extensions/portalops-*
  - portalops-ai/**
alwaysApply: true
---

# PortalOps AI Rules

## Product Identity
PortalOps AI is an enterprise portal operations platform for Liferay 7.4.

This is NOT:
- a generic chatbot
- an FAQ assistant
- a demo AI playground

This IS:
- portal operations intelligence
- governance tooling
- workflow visibility
- permissions analysis
- content governance
- operational dashboards
- controlled action execution

---

## Product Scope
Focus on portal management capabilities:

- workflow operations
- permissions governance
- content hygiene
- site intelligence
- search diagnostics
- portal administration
- compliance insights

Example user intents:
- Show pending workflows
- Show stale content
- Show risky permissions
- Show who can publish a page
- Show orphaned pages
- Export governance reports

Future controlled actions:
- reassign workflows
- archive stale drafts
- trigger scans
- create cleanup tasks

---

## Architecture
PortalOps AI uses hybrid architecture.

### Liferay Layer
Use Liferay for:
- UI shell
- authentication
- RBAC integration
- dashboard hosting
- portal integration
- client extensions
- REST bridges

Prefer:
- Client Extensions
- Remote Apps
- supported APIs

Avoid modifying Liferay core.

---

### AI Backend
AI logic must live in separate Spring Boot services.

Preferred stack:
- Java 21
- Spring Boot
- Spring AI

Reason:
Avoid heavy AI dependency coupling inside OSGi modules.

---

## Modularity
Design as modular capability packs.

Examples:

Core:
- portalops-core
- portalops-command-router
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

Adapters:
- portalops-liferay-adapter
- portalops-llm-spi
- portalops-vector-spi

---

## AI Interaction Model
PortalOps AI is not chat-first.

Supported interaction:
- slash commands
- natural language requests
- dashboards
- operational reports
- approval workflows

Example commands:
- /show workflows pending
- /show permissions risky
- /show stale content
- /show site anomalies

Commands should route through command handlers.

---

## MVP Rules
Initial MVP must be READ ONLY.

Allowed:
- inspections
- analysis
- summaries
- reports

Disallowed:
- deleting content
- modifying permissions
- bulk publishing
- destructive automation

Add write actions only after approval workflow exists.

---

## AI Usage
Use LLM reasoning only when useful:
- summarization
- explanation
- recommendations
- classification

Do NOT use LLM for deterministic retrieval.

Example:
Query workflows using APIs, then summarize using AI.

---

## Security
Enterprise-first requirements:
- audit logging
- RBAC awareness
- action approval controls
- traceability
- no hidden side effects

---

## Code Design
Prefer:
- interface-driven architecture
- clean modular design
- provider abstraction
- adapter pattern
- testable services

Avoid:
- giant controllers
- business logic in UI layers
- hardcoded provider logic
- direct API sprawl

---

## Future Extensibility
Support pluggable providers.

LLM:
- OpenAI
- Azure OpenAI
- Anthropic
- AWS Bedrock

Vector:
- pgvector
- Pinecone
- OpenSearch
- Azure AI Search