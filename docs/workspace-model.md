# PortalOps Workspace Model

PortalOps should render investigations into operational workspaces.

The AI does not generate screens directly.

The AI and investigation runtime produce structured outcomes. PortalOps renders those outcomes.

## Workspace Principle

PortalOps follows this model:

```text
Intent -> Investigation -> Structured Outcome -> Workspace -> Actions
```

## Structured Outcomes

Future outcomes may contain:

- Summary
- Findings
- Recommendations
- Actions
- Workspace Components

The workspace engine is responsible for rendering these into usable administrative experiences.

## Workspace Components

PortalOps may render:

- Cards
- Tables
- Trees
- Forms
- Charts
- Findings
- Actions

These are rendered components, not AI-generated page source.

## Workspace Responsibilities

The workspace engine should:

- Render structured outcomes consistently
- Preserve governance-aware action semantics
- Support multiple investigation types
- Reuse common components across modules
- Keep findings and actions visible and easy to scan

## Relationship to Dashboard

The dashboard remains a supporting view.

The long-term center of the product is the workspace model driven by assistant-led investigations and structured outcomes.
