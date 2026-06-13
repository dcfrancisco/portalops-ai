# Privacy and Data Exposure

PortalOps is an operational intelligence platform, not a user monitoring tool.

## Default Data Strategy

Prefer aggregated information over individual records whenever possible.

Prefer:

- counts
- summaries
- trends
- health indicators
- anomalies
- aggregate operational observations

Avoid exposing:

- personal information
- detailed activity history
- login history
- email addresses
- user identities
- memberships
- roles
- permissions

## When Individual Details Are Allowed

Expose individual user details only when:

- the user explicitly asks for them
- the details are necessary to answer the question
- the details are required for an administrative action

## Example Behaviors

User: `How many users do we have?`

Return:

- aggregate count only

User: `Tell me about portal usage.`

Return:

- traffic
- trends
- summaries
- operational observations

User: `Show inactive users.`

Return:

- the specific users relevant to the request

## Principle

Do not expose more data than is needed to help the administrator act.
