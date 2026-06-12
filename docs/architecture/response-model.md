# Response Model

PortalOps responses are structured operational outcomes rendered inside a workspace.

PortalOps does not rely solely on narrative text. Investigations should return structured data that supports analysis, decision making, and action.

## Response Shape

PortalOps investigations may produce:

- Summary
- Findings
- Recommendations
- Actions
- Workspace Components

The canonical order remains:

1. Summary
2. Findings
3. Recommendations
4. Actions
5. Supporting workspace components when needed

Detailed narrative explanation is secondary.

## PortalOpsAnalysisResponse

`PortalOpsAnalysisResponse` is the provider-independent contract for structured investigation outcomes.

At minimum, it should support:

- Summary
- Findings Cards
- Recommendations
- Actions

As the workspace model matures, the response can expand to include additional structured workspace elements without collapsing into free-form chat text.

## Findings

Findings identify the problem, signal, risk, or anomaly discovered during an investigation.

Examples:

- Search failures detected after recent content changes
- Elevated permission assignments on sensitive resources
- Stale content lacking active ownership
- Workflow failures affecting scheduled publishing

Findings should be concrete, operational, and easy to scan.

## Recommendations

Recommendations translate findings into next steps.

Examples:

- Review recent permission changes
- Confirm content ownership
- Inspect failed workflow retries
- Validate search indexing health

Recommendations should remain subordinate to governance and organizational policy.

## Actions

Actions move users into the correct PortalOps workspace or module.

Examples:

- Open Audit
- Review Content
- Open Workflow
- Review System Health

Actions should accelerate navigation and operations. They should not trap users inside conversational loops.

## Workspace Components

PortalOps responses may render as workspace components such as:

- Cards
- Tables
- Trees
- Forms
- Charts
- Findings panels
- Action panels

AI does not generate UI directly. It contributes structured outcomes that the PortalOps renderer translates into these components.

## Reusable Components

Cards and related components are reusable operational primitives.

Examples:

- `SearchFailureCard`
- `StaleContentCard`
- `PermissionRiskCard`
- `WorkflowFailureCard`
- `ConfigurationDriftCard`
- `PolicyViolationCard`

These components should be reusable across:

- Overview
- Assistant
- Reports
- Notifications
- Future workspaces
