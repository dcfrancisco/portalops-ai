# Insights and Recommendations

Insights and recommendations are optional.

## Insights

Only render insights when meaningful insight cards exist.

If no insights are available:

- do not render an empty insights section
- do not render placeholder text

Insights should be concise and operational.

## Recommendations

Generate recommendations only when:

- analysis was requested
- risks were detected
- operational concerns were identified

Do not generate recommendations for simple fact retrieval.

## Examples

User: `How many users do we have?`

- Return the count
- Do not add recommendations

User: `Tell me about the users.`

- Return the operational picture
- Add recommendations only if the data reveals a concern
