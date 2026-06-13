# PortalOps Core Domain

PortalOps treats the Core domain as runtime self-discovery for administrators.

## Runtime Mapping

- Domain: `PortalOps`
- Agent: `PortalOpsManagementAgent`
- Skills:
  - `ListCapabilities`
  - `DescribeCapability`
  - `ListDomains`
  - `ListAgents`
  - `ListSkills`
- Tool: `GetRuntimeMetadataTool`

## Administrator Capabilities

- List PortalOps capabilities
- List PortalOps domains
- List PortalOps agents
- List PortalOps skills
- Describe a PortalOps capability or domain

## Implementation Note

Capabilities are what administrators see.

Agents, skills, and tools remain internal PortalOps implementation details used to collect structured data before the model generates the final response.
