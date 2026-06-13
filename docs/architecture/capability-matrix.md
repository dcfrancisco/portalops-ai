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
| Active Users | UserManagementAgent | GetActiveUsers | Planned |
| Inactive Users | UserManagementAgent | GetInactiveUsers | Planned |
| Locked Users | UserManagementAgent | GetLockedUsers | Planned |
| Administrator Review | UserManagementAgent | GetAdministrators | Planned |

---

# Sites Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Site Count | SiteManagementAgent | GetSites | Planned |
| List Sites | SiteManagementAgent | GetSites | Planned |
| Site Membership | SiteManagementAgent | GetSiteMembership | Planned |
| Site Activity | SiteManagementAgent | GetSiteActivity | Planned |

---

# Search Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Search Health | SearchManagementAgent | GetSearchHealth | Planned |
| Reindex Status | SearchManagementAgent | GetReindexStatus | Planned |
| Search Errors | SearchManagementAgent | GetSearchErrors | Planned |

---

# Content Domain

| Capability | Agent | Skill | Status |
| --- | --- | --- | --- |
| Content Summary | ContentManagementAgent | GetContentSummary | Planned |
| Expired Content | ContentManagementAgent | GetExpiredContent | Planned |
| Pending Content | ContentManagementAgent | GetPendingContent | Planned |

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
| List Agents | PortalOpsManagementAgent | ListAgents | Planned |
| List Skills | PortalOpsManagementAgent | ListSkills | Planned |
| List Capabilities | PortalOpsManagementAgent | ListCapabilities | Planned |
| Describe Capability | PortalOpsManagementAgent | DescribeCapability | Planned |

---

# Insights

| Domain | Status |
| --- | --- |
| User Insights | Planned |
| Site Insights | Planned |
| Search Insights | Planned |
| Content Insights | Planned |

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
