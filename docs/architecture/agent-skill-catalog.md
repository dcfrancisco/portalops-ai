# PortalOps Agent and Skill Catalog

Status: Proposed. Decisions: [ADR-0020](../adr/0020-agent-and-skill-catalog.md) (the catalog) and [ADR-0021](../adr/0021-agent-skill-lifecycle-management.md) (adding, changing and removing agents and skills).

This is the starting catalog. It is not closed: administrators and the Agent OS can add, change and remove agents and skills under governance (ADR-0021). Every entry registers through the capability contract (ADR-0004), is subject to policy (ADR-0005, ADR-0011), and shows in the generated [capability matrix](capability-matrix.md).

## Vocabulary

| Term | Meaning |
| --- | --- |
| Agent | A named specialist with a domain scope, a goal, a set of skills, a policy envelope (max risk tier, max autonomy, budget) and an owner. |
| Skill | A reusable, bounded procedure that composes tools and decision references to deliver one outcome. |
| Tool | An atomic, schema-described operation on an adapter, store or device (the syscall, ADR-0004). |
| Playbook | A versioned plan template that chains skills for a recurring situation. |

Skill kinds:

| Kind | Authored by | Can introduce new effects? |
| --- | --- | --- |
| Coded | Developers (OSGi bundle or SDK extension, ADR-0019) | Yes, after review and certification |
| Declarative | Administrators or the Agent OS (manifest: tools, decision tables, prompt template, output schema) | No, composes existing tools only |
| Playbook | Administrators or the Agent OS | No, chains existing skills |
| External | Third-party agents over MCP or A2A (ADR-0012) | Treated as untrusted, tier and effect set by an admin |

Effect and tier legend used below: `R0` read-only, `R1` reversible low impact, `R2` reversible high impact, `R3` destructive. Writes default to autonomy L2 (approval required). Tier is the default, and the decision model (ADR-0011) can raise it per context.

## 1. System agents (kernel-level, always present)

| Agent | Purpose | Key skills | Max tier |
| --- | --- | --- | --- |
| **Orchestrator** | Resolves intent, builds and revises plans, manages child runs. | `ResolveIntent`, `BuildPlan`, `ReplanOnObservation`, `DelegateSubRun` | R0 (plans only) |
| **Critic / Verifier** | Independent review of plans and results, post-action verification. | `ReviewPlan`, `ChallengeFinding`, `VerifyOutcome`, `DetectContradiction` | R0 |
| **Governance Advisor** | Explains policy outcomes, drafts policy and decision-table changes for review. | `ExplainDecision`, `SimulatePolicyChange`, `DraftDecisionTable`, `ListPolicyExceptions` | R0 |
| **Knowledge Librarian** | Retrieval, citations, ingestion health, coverage gaps. | `SearchKnowledge`, `CiteSources`, `IngestSource`, `FindKnowledgeGaps` | R1 (ingestion) |
| **Memory Curator** | Compacts memory, links related runs, proposes playbooks from repeated work. | `SummarizeEpisodes`, `DedupeMemory`, `ProposePlaybook`, `ApplyRetention` | R1 |
| **Registry Steward** (existing `PortalOpsManagementAgent`) | Catalog introspection for users and the planner. | `ListAgents`, `ListSkills`, `ListCapabilities`, `DescribeCapability`, `ListDomains`, `ExplainCapabilityUsage` | R0 |
| **Agent Architect** | Designs new agents, skills and playbooks from observed gaps. Produces DRAFTS only (ADR-0021). | `DetectCapabilityGap`, `DraftSkill`, `DraftAgent`, `DraftPlaybook`, `GenerateSkillTests`, `ProposeRetirement` | R0 (proposals only) |
| **Quality Steward** | Runs evaluations, reviews failures, tracks drift. | `RunEvalSuite`, `ReviewRunSample`, `ReportDrift`, `RunRedTeamSuite` | R0 |
| **Budget Steward** | Cost, token and carbon visibility, budget enforcement advice. | `ReportUsage`, `ForecastSpend`, `RecommendRouting`, `FlagCostAnomaly` | R0 |
| **Briefing Agent** | Daily and on-demand briefings for screen and voice. | `BuildBriefing`, `SummarizeOvernight`, `PrioritizeFindings`, `RenderSpokenBrief` | R0 |

## 2. Domain agents (portal operations)

Existing skills are marked **(existing)** and map to current modules.

| Agent | Read skills (R0) | Operate skills (default L2) |
| --- | --- | --- |
| **Identity and Access** (`UserManagementAgent`) | `GetUsers` (existing), `GetActiveUsers` (existing), `GetInactiveUsers` (existing), `GetLockedUsers` (existing), `GetAdministrators` (existing), `ReviewRoleAssignments`, `FindExcessivePermissions`, `MapAccountsToOrganizations`, `ReviewPasswordPolicyCompliance`, `DetectSegregationOfDutyConflicts` | `DeactivateStaleUsers` (R2), `UnlockUser` (R1), `RemoveRoleAssignment` (R2), `RequestAccessRecertification` (R1) |
| **Site and Page** (`SiteManagementAgent`) | `GetSites` (existing), `FindOrphanedPages`, `FindSitesWithoutPages`, `AnalyzePageHierarchy`, `ReviewSiteMembership`, `DetectTemplateDrift` | `PublishPage` / `UnpublishPage` (R2), `FixNavigationLinks` (R1), `ApplySiteTemplate` (R2) |
| **Content** (`ContentManagementAgent`) | `GetContentSummary` (existing), `GetExpiredContent` (existing), `GetPendingContent` (existing), `FindStaleContent`, `FindUnpublishedDrafts`, `AnalyzeDocumentLibraryUsage`, `ReviewScheduledPublishing`, `FindDuplicateContent` | `ExpireContent` (R2), `ReassignOwner` (R1), `RequestContentReview` (R1), `BulkArchive` (R3) |
| **Workflow** | `GetWorkflowSummary`, `GetPendingTasks`, `FindStuckInstances`, `AnalyzeBacklog`, `ForecastSLABreaches` | `ReassignTask` (R1), `NudgeAssignee` (R1), `ResolveStuckInstance` (R2) |
| **Search** (`SearchManagementAgent`) | `GetSearchHealth` (existing), `GetReindexStatus` (existing), `GetSearchErrors` (existing), `AnalyzeQueryFailures`, `CheckIndexFreshness`, `ReviewRelevanceSignals` | `TriggerReindex` (R2), `ProposeBlueprintChange` (proposal only) |
| **Platform Health** | `GetSchedulerStatus`, `ReviewBackgroundTasks`, `DetectConfigurationDrift`, `AnalyzeLogs`, `ReviewInstanceSettings`, `InventoryVirtualInstances`, `CheckDependencyHealth` | `RetryBackgroundTask` (R1), `ProposeConfigurationChange` (proposal only) |
| **Audit and Compliance** | `AnalyzeAuditEvents`, `CheckPolicyConformance`, `BuildEvidencePack`, `ReviewAccessChanges`, `TraceDataSubjectData` | `ScheduleComplianceRun` (R1), `ExecuteErasureRequest` (R3, four-eyes) |
| **Security Posture** | `ReviewExposedEndpoints`, `CheckPatchAndVersionStatus`, `DetectAnomalousAccess`, `ReviewSecretsAndKeys` | `RevokeSession` (R1), `RotateCredentialRequest` (R2) |
| **Performance** | `AnalyzePageLoad`, `FindSlowQueries`, `ReviewCachingConfig`, `CapacityForecast` | `ProposeTuningChange` (proposal only) |
| **Localization and Accessibility** | `FindUntranslatedContent`, `CheckAltTextAndContrast`, `ReviewLocaleCoverage` | `RequestTranslation` (R1) |

## 3. Sensory and physical agents

| Agent | Purpose | Key skills | Max tier |
| --- | --- | --- | --- |
| **Voice Agent** | Speech in and out, clarification, spoken briefings and approval read-back. Cannot approve (ADR-0009). | `Transcribe`, `ClarifyIntent`, `SpeakOutcome`, `ReadBackProposal` | R0 |
| **Vision Agent** | Understands screenshots, documents, charts and diagrams. | `ExtractFromImage`, `ReadDocument`, `DescribeChart`, `CompareScreenshots` | R0 |
| **Visual QA Agent** | Renders portal pages, detects layout, media and brand regressions and accessibility issues. | `CapturePage`, `DetectVisualRegression`, `CheckBrandStandards`, `ReportAccessibilityIssues` | R0 |
| **Facility and Edge Agent** | Device fleet health, sensor correlation with portal incidents (ADR-0010). | `GetDeviceHealth`, `CorrelateSensorIncident`, `ReviewZonePolicy`, `ForecastFacilityRisk` | R0 |
| **Signage and Announcement Agent** | Approved announcements and kiosk or signage content. | `PlanAnnouncement`, `PublishSignageContent` (R2), `PlayAnnouncement` (R2) | R2, never above |

## 4. Integration agents

| Agent | Purpose | Key skills | Max tier |
| --- | --- | --- | --- |
| **Notification Agent** | Routes notifications and approval requests over channels (ADR-0019). | `Notify`, `RequestApprovalViaChannel`, `Escalate` | R1 |
| **Ticketing Agent** | ITSM and issue-tracker integration. | `CreateTicket`, `UpdateTicket`, `LinkEvidence` | R1 |
| **External Agent Gateway** | Mediates MCP tools and A2A agents (ADR-0012). | `DiscoverExternalCapabilities`, `InvokeExternal`, `QuarantineExternal` | Set per external capability |

## Common skills (shared building blocks)

Reusable by every agent: `Summarize`, `Correlate`, `RankFindings`, `AttachEvidence`, `ExplainWithCitations`, `EstimateImpact` (uses the twin, ADR-0014), `ProposeAction`, `VerifyState`, `GenerateReport`, `RedactForExport`.

## Skill specification (what a skill must declare)

```text
name, version, kind, owner, domain, description, examplePrompts[]
inputs / outputs        (JSON Schema)
tools[]                 (references to registered tools)
decisions[]             (decision-table references, ADR-0011)
promptTemplate          (optional, declarative skills)
effect, riskTier, requiredPermissions[] (computed from tools, cannot be lowered)
budget                  (steps, tokens, time, cost)
tests[]                 (evaluation scenarios, ADR-0013) - required
simulationSupport       (ADR-0014)
status                  (lifecycle state, ADR-0021)
```

An agent manifest declares: name, purpose, domain scope, skills, maximum risk tier, maximum autonomy, budget envelope, allowed data classes, allowed modalities, owner, and escalation contact.

## Discovery at scale

The planner does not receive the full catalog. Capabilities are embedded (pgvector, ADR-0006) from their descriptions and example prompts. The intent resolver retrieves the top candidates that the requester is permitted to use, so the catalog can grow to hundreds of skills without overflowing the model context.

## Initial priority

| Release | Agents |
| --- | --- |
| R1 | Orchestrator, Critic, Registry Steward, Knowledge Librarian, Quality Steward, Identity, Site, Content, Search, Workflow (read), Platform Health (read), Audit and Compliance (read) |
| R2 | Write skills for Identity, Content, Workflow, Search, Platform Health; Notification, Ticketing, Governance Advisor, Briefing, Budget Steward |
| R3 | Voice, Vision, Visual QA, External Agent Gateway, Security Posture, Performance, Localization and Accessibility |
| R4 | Agent Architect, Memory Curator, multi-agent critic roles, scheduled and event playbooks |
| R5 | Facility and Edge, Signage and Announcement |
