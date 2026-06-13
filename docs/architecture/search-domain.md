# Search Domain

PortalOps treats the Search domain as an administrator-facing read-only diagnostics area.

## Runtime Mapping

- Domain: `Search`
- Agent: `SearchManagementAgent`
- Skills:
  - `GetSearchHealth`
  - `GetReindexStatus`
  - `GetSearchErrors`
- Tool: `GetSearchTool`

## Administrator Capabilities

- Review search health
- Review reindex status
- Review search diagnostics and warnings

## Implementation Note

Capabilities are what administrators see.

Agents, skills, and tools remain internal PortalOps implementation details used to collect structured data before the model generates the final response.
