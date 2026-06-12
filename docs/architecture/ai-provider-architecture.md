# AI Provider Architecture

PortalOps uses AI providers as reasoning and analysis implementations inside a larger operational intelligence architecture.

Providers do not own the PortalOps product experience. PortalOps owns the investigation model, governance boundaries, response structure, workspace rendering, and actions.

## Core Platform Components

PortalOps architecture includes:

- OpenAI Provider
- Vector Database
- Knowledge Engine
- Governance Engine
- Investigation Engine
- Workspace Renderer

These components work together to translate intent into operational investigations and structured outcomes.

## Provider Contract

`portalops-ai-api` contains the provider-independent contracts:

- `PortalOpsAiProvider`
- `PortalOpsAnalysisRequest`
- `PortalOpsAnalysisResponse`
- `FindingCard`
- `Recommendation`
- `ActionLink`

Provider interface:

```java
public interface PortalOpsAiProvider {

    String getProviderName();

    PortalOpsAnalysisResponse analyze(
        PortalOpsAnalysisRequest request);

}
```

## Ownership Boundaries

PortalOps owns:

- Intent handling
- Investigation structure
- Governance enforcement
- Response structure
- Card taxonomy
- Recommendations
- Actions
- Navigation semantics
- Workspace rendering

AI providers contribute:

- Reasoning
- Correlation
- Summarization
- Retrieval-assisted analysis

Providers must return analysis using PortalOps-owned contracts.

## OpenAI First

Initial provider bundle:

- `portalops-ai-openai`

This is the first provider integration target.

Provider credentials can be introduced later through environment-aware configuration, but the PortalOps response model remains provider-independent.

## Vector Database and Retrieval

PortalOps knowledge is intended to be indexed into a vector database for retrieval.

Potential knowledge sources include:

- Liferay Documentation
- Internal Runbooks
- Operational Procedures
- Governance Policies
- Portal Administration Guides
- Knowledge Base Articles

AI providers should consume retrieved knowledge through PortalOps investigation services rather than owning knowledge storage semantics themselves.

## Future Providers

Future providers should be separate OSGi bundles, for example:

- `portalops-ai-claude`
- `portalops-ai-ollama`
- `portalops-ai-gemini`
- `portalops-ai-oip`

Each provider should plug into the same PortalOps contracts without changing the PortalOps workspace or response shape.

## Provider Independence

Regardless of provider, the rendered PortalOps experience should remain structurally consistent:

1. Summary
2. Findings
3. Recommendations
4. Actions
5. Workspace components as needed

PortalOps owns the user experience. Providers supply analysis inside that experience.
