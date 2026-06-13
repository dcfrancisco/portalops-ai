# Content Domain

PortalOps treats the Content domain as an administrator-facing read-only capability area.

## Runtime Mapping

- Domain: `Content`
- Agent: `ContentManagementAgent`
- Skills:
  - `GetContentSummary`
  - `GetExpiredContent`
  - `GetPendingContent`
- Tool: `GetContentTool`

## Administrator Capabilities

- Summarize portal content
- Review expired content
- Review pending and draft content

## Implementation Note

Capabilities are what administrators see.

Agents, skills, and tools remain internal PortalOps implementation details used to collect structured data before the model generates the final response.
