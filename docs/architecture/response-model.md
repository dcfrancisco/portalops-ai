# Response Model

PortalOps responses are structured operational outputs.

PortalOps does not answer questions primarily with paragraphs. It answers with findings, reusable cards, recommendations, and actions.

## PortalOpsAnalysisResponse

The provider-independent analysis response model is:

- Summary
- Findings Cards
- Recommendations
- Actions

This structure should remain stable regardless of which AI provider is used.

## Response Order

Operations engineers need:

1. Problem
2. Impact
3. Action

before detailed explanation.

That means the response should be rendered in this order:

1. Summary
2. Findings Cards
3. Recommendations
4. Actions

Detailed narrative explanation is secondary.

## Findings Cards

Cards are reusable response components, not navigation components.

Examples:

- `SearchFailureCard`
- `StaleContentCard`
- `PermissionRiskCard`
- `WorkflowFailureCard`
- `ConfigurationDriftCard`
- `PolicyViolationCard`

Cards should be reusable across:

- Overview
- Assistant
- Reports
- Notifications

## Recommendations

Recommendations translate findings into suggested next steps.

Examples:

- Review search indexing
- Assign content owners
- Review permission changes
- Investigate failed workflows

Recommendations should be operationally useful and easy to act on.

## Actions

Actions should navigate into PortalOps modules.

Examples:

- Open Search Health
- Review Stale Content
- Open Permission Risks

Actions should not trap users inside assistant-style conversations.

PortalOps modules remain first-class operational destinations.
