# PortalOps Capability Matrix

## Status Legend

| Status | Meaning |
| --- | --- |
| Planned | Not started |
| In Progress | Partially implemented |
| Implemented | Available and working |
| Future | Post-MVP |

---

# Users Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| User Count | UserManagementAgent | GetUsers | Implemented |
| List Users | UserManagementAgent | GetUsers | Implemented |
| User Summary | UserManagementAgent | GetUsers | Implemented |
| User Roles | UserManagementAgent | GetUsers | Implemented |
| User Organizations | UserManagementAgent | GetUsers | Implemented |
| User Groups | UserManagementAgent | GetUsers | Implemented |
| Active Users | UserManagementAgent | GetActiveUsers | Implemented |
| Inactive Users | UserManagementAgent | GetInactiveUsers | Implemented |
| Locked Users | UserManagementAgent | GetLockedUsers | Implemented |
| Administrator Review | UserManagementAgent | GetAdministrators | Implemented |

---

# Sites Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Site Count | SiteManagementAgent | GetSites | Implemented |
| List Sites | SiteManagementAgent | GetSites | Implemented |
| Site Summary | SiteManagementAgent | GetSites | Implemented |
| Page Counts | SiteManagementAgent | GetSites | Implemented |
| List Pages by Site | SiteManagementAgent | GetSites | Implemented |
| List Site and Page Names | SiteManagementAgent | GetSites | Implemented |
| Site Activity | SiteManagementAgent | GetSites | Implemented |
| Page Details | SiteManagementAgent | GetSites | Planned |
| Page Hierarchy Analysis | SiteManagementAgent | GetSites | Planned |
| Sites Without Pages | SiteManagementAgent | GetSites | Planned |
| Site Membership Analysis | SiteManagementAgent | GetSites | Planned |

---

# Search Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Search Health | SearchManagementAgent | GetSearchHealth | Implemented |
| Reindex Status | SearchManagementAgent | GetReindexStatus | Implemented |
| Search Errors | SearchManagementAgent | GetSearchErrors | Implemented |

---

# Content Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Content Summary | ContentManagementAgent | GetContentSummary | Implemented |
| Expired Content | ContentManagementAgent | GetExpiredContent | Implemented |
| Pending Content | ContentManagementAgent | GetPendingContent | Implemented |

---

# Workflow Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Active Workflows | WorkflowManagementAgent | GetWorkflowSummary | Planned |
| Pending Tasks | WorkflowManagementAgent | GetPendingTasks | Planned |

---

# PortalOps Core

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| List Agents | PortalOpsManagementAgent | ListAgents | Implemented |
| List Skills | PortalOpsManagementAgent | ListSkills | Implemented |
| List Capabilities | PortalOpsManagementAgent | ListCapabilities | Implemented |
| Describe Capability | PortalOpsManagementAgent | DescribeCapability | Implemented |
| List Domains | PortalOpsManagementAgent | ListDomains | Implemented |

---

# Insights

| Domain | Status |
| --- | --- |
| User Insights | Implemented |
| Site Insights | Implemented |
| Search Insights | Implemented |
| Content Insights | Implemented |

---

# Actions

| Domain | Status |
| --- | --- |
| User Actions | Future |
| Site Actions | Future |
| Search Actions | Future |

---

# Liferay Opportunities

These are platform-native capability areas PortalOps can add after the current MVP without changing the Agent -> Skill -> Tool model.

## Identity and Access

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Role Membership Review | `RoleLocalService`, `UserGroupRoleLocalService` | Useful for governance and administrator review flows. |
| Account and Organization Mapping | `AccountEntry`, `OrganizationLocalService` | Important for B2B and multi-org installations. |
| Password Policy and Lockout Review | `PasswordPolicy`, `UserLocalService` | Supports security posture and user access analysis. |

## Sites and Information Architecture

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Site Template Usage | `GroupLocalService`, site templates APIs | Helps identify inheritance and drift across sites. |
| Page Inventory | `LayoutLocalService` | Supports page count, orphan analysis, and IA review. |
| Site Permissions Review | `ResourcePermissionLocalService` | Natural extension for governance-focused insights. |

## Content and Publishing

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Web Content Inventory | `JournalArticleLocalService` | Foundation for content summaries and lifecycle insights. |
| Document Library Analysis | `DLAppService`, `DLFileEntryLocalService` | Useful for stale files, ownership, and storage insights. |
| Scheduled Publishing Review | `JournalArticle`, workflow and scheduler data | Helps identify delayed or risky publishing windows. |

## Search and Discovery

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Index Freshness | search engine adapter APIs, reindex services | Useful for health, drift, and delayed indexing insights. |
| Query Failure Analysis | search logs, search tuning APIs | Supports operational troubleshooting and relevance review. |
| Result Quality Signals | search tuning, Blueprints, analytics data | Good future area once health checks are stable. |

## Workflow and Operations

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Workflow Backlog Analysis | Kaleo task and instance services | Supports pending task and stuck workflow investigations. |
| Scheduler and Background Task Review | `SchedulerEngineHelper`, `BackgroundTaskLocalService` | Strong fit for operations-focused insights. |
| Configuration Drift Detection | Configuration Admin, OSGi configs, portal properties | Useful for governance and runtime diagnostics. |

## Platform and Portal Governance

| Capability | Likely Liferay Sources | Notes |
| --- | --- | --- |
| Instance Settings Review | instance settings APIs, configuration models | Good fit for assistant explanations and recommendations. |
| Virtual Instance Inventory | `CompanyLocalService` | Helps multi-tenant operators understand company scope. |
| Audit Event Analysis | audit framework services | Natural source for compliance and operational intelligence. |
