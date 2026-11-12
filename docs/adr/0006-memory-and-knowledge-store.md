# ADR-0006: PostgreSQL with pgvector for memory and knowledge

Status: Accepted (PostgreSQL with pgvector confirmed by the project owner)

## Context

Documentation conflicts: `docs/ai-architecture.md` names ChromaDB as the vector database, while `docs/ROADMAP.md` Phase 2 names PostgreSQL and pgvector. `portalops-knowledge` currently builds a snapshot and does no retrieval. The Agent OS also needs run persistence, episodic memory, approvals and audit storage.

## Decision

Use PostgreSQL as the single store, with the `pgvector` extension for embeddings. This supersedes the ChromaDB direction.

Memory tiers: working (in-process), episodic (runs, outcomes, human corrections), semantic (docs, runbooks, policies), procedural (versioned, human-approved playbooks).

- Data is partitioned per virtual instance (company id). Retrieval applies policy: derived knowledge is only returned if the requester could read the source.
- Embedding model is behind the provider SPI and recorded with each vector, so re-embedding is possible when it changes.
- Ingestion is a scheduled and event-driven run like any other, and is audited.
- Learned procedures are never auto-reused. They need approval and are versioned.
- Transactional data (runs, approvals, audit) and vectors share one database for simple consistency and operations.

Required components (detailed in the [design](../architecture/agent-os.md)):

- `portalops-persistence`: pooled DataSource, versioned migrations, extension and health checks, HNSW index management, backup guidance.
- `portalops-embedding`: embedding calls via the provider SPI, model and version tracked per vector, re-embedding support.
- `portalops-ingest`: connectors, parsing, chunking, content-hash de-duplication, ACL capture, incremental sync and deletion propagation.
- `portalops-knowledge`: hybrid retrieval (vector plus full-text), metadata filters, re-ranking, citations, policy filtering.
- `portalops-memory`: tiered memory API, retention and compaction.
- `portalops-artifact`: stores multimodal artifacts (ADR-0009), with caption and OCR embeddings indexed here.
- Row-level security keyed on `company_id` for instance isolation.
- PostgreSQL with pgvector added to `compose.yaml` and the environment configs.

## Consequences

- Positive: one operational dependency, transactional consistency, familiar backup and HA.
- Negative: PostgreSQL becomes required, in addition to Liferay's own database. A separate schema and connection pool are needed.
- Negative: very large corpora may need tuning (HNSW index). Revisit if scale demands a dedicated vector store.
- Docs and compose files must be updated to match.
