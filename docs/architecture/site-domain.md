# Sites Domain

PortalOps treats the Sites domain as an administrator-facing read-only capability area.

## Runtime Mapping

- Domain: `Sites`
- Agent: `SiteManagementAgent`
- Skill: `GetSites`
- Tool: `GetSitesTool`

## Administrator Capabilities

- List sites
- Count sites
- Summarize site/page structure
- List public pages grouped by site
- List private pages grouped by site
- List site and page names

## Implementation Note

Capabilities are what administrators see.

Agents, skills, and tools are internal PortalOps implementation details used to collect structured data before the model generates the final response.
