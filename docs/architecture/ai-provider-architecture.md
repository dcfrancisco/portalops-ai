# AI Provider Architecture

PortalOps uses a provider abstraction for analysis.

Providers are analysis implementations only. They do not own the PortalOps response model or user experience.

## Core Contract

`portalops-ai-api` contains:

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

- Response structure
- Card taxonomy
- Recommendations
- Actions
- Navigation semantics

AI providers own:

- Analysis
- Correlation
- Summarization

Providers must return analysis using the PortalOps contract.

## Initial Provider

Initial provider bundle:

- `portalops-ai-openai`

This is the only provider that should be implemented at this stage.

## Future Providers

Future providers should be separate OSGi bundles:

- `portalops-ai-claude`
- `portalops-ai-ollama`
- `portalops-ai-gemini`
- `portalops-ai-oip`

Each provider should plug into the same PortalOps-owned contract without changing the user experience.

## Provider Independence

Whether the provider is:

- OpenAI
- Claude
- Ollama
- OIP

the rendered PortalOps experience should remain:

1. Summary
2. Findings Cards
3. Recommendations
4. Actions

PortalOps owns the user experience. Providers only perform analysis.
