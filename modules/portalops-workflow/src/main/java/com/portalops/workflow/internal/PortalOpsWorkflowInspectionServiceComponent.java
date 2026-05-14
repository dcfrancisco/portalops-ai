package com.portalops.workflow.internal;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.kernel.workflow.WorkflowInstanceManager;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskAssignee;
import com.liferay.portal.kernel.workflow.WorkflowTaskManager;

import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.api.workflow.WorkflowInspectionResult;
import com.portalops.api.workflow.WorkflowPendingSummary;
import com.portalops.api.workflow.WorkflowInspectionService;
import com.portalops.api.workflow.WorkflowSummary;
import com.portalops.api.workflow.WorkflowTaskSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = WorkflowInspectionService.class)
public class PortalOpsWorkflowInspectionServiceComponent
        implements WorkflowInspectionService {

    @Override
    public WorkflowInspectionResult inspectPendingWorkflows(
            PortalOpsRequestContext context) {

        try {
            Map<Long, WorkflowTask> workflowTasks = new LinkedHashMap<>();

            _addWorkflowTasks(
                    workflowTasks,
                    _workflowTaskManager.getWorkflowTasksByUser(
                            context.getCompanyId(), context.getUserId(), false,
                            QueryUtil.ALL_POS, QueryUtil.ALL_POS, null));
            _addWorkflowTasks(
                    workflowTasks,
                    _workflowTaskManager.getWorkflowTasksByUserRoles(
                            context.getCompanyId(), context.getUserId(), false,
                            QueryUtil.ALL_POS, QueryUtil.ALL_POS, null));

            List<WorkflowTaskSummary> workflowTaskSummaries = new ArrayList<>();

            for (WorkflowTask workflowTask : workflowTasks.values()) {
                WorkflowInstance workflowInstance = _getWorkflowInstance(
                        context.getCompanyId(), workflowTask.getWorkflowInstanceId());

                if (!_matchesScopeGroup(context, workflowInstance)) {
                    continue;
                }

                workflowTaskSummaries.add(
                        _toWorkflowTaskSummary(workflowTask, workflowInstance));
            }

            return new WorkflowInspectionResult(
                    _summarizeByAssignee(workflowTaskSummaries),
                    _summarizeByWorkflowDefinition(workflowTaskSummaries),
                    _getScopeGroupName(context.getGroupId()), workflowTaskSummaries);
        } catch (WorkflowException workflowException) {
            _log.error("Unable to inspect pending workflow tasks", workflowException);

            return new WorkflowInspectionResult(
                    Collections.emptyList(), Collections.emptyList(),
                    _getScopeGroupName(context.getGroupId()),
                    Collections.emptyList());
        }
    }

    @Override
    public List<WorkflowSummary> getPendingWorkflows(
            PortalOpsRequestContext context) {

        List<WorkflowSummary> workflowSummaries = new ArrayList<>();

        for (WorkflowTaskSummary workflowTaskSummary : inspectPendingWorkflows(context).getWorkflowTaskSummaries()) {

            workflowSummaries.add(
                    new WorkflowSummary(
                            workflowTaskSummary.getWorkflowTaskId(),
                            workflowTaskSummary.getAssetTitle(),
                            workflowTaskSummary.getWorkflowTaskName(),
                            workflowTaskSummary.getAssigneeName()));
        }

        return workflowSummaries;
    }

    @Override
    public List<WorkflowSummary> getStuckWorkflows(
            PortalOpsRequestContext context) {

        return Collections.emptyList();
    }

    private void _addWorkflowTasks(
            Map<Long, WorkflowTask> workflowTasks, List<WorkflowTask> workflowTaskList) {

        for (WorkflowTask workflowTask : workflowTaskList) {
            workflowTasks.put(workflowTask.getWorkflowTaskId(), workflowTask);
        }
    }

    private String _getAssetTitle(
            WorkflowTask workflowTask, WorkflowInstance workflowInstance) {

        if (workflowInstance != null) {
            Map<String, Serializable> workflowContext = workflowInstance.getWorkflowContext();

            String entryClassName = GetterUtil.getString(
                    workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));
            String entryClassPK = GetterUtil.getString(
                    workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));

            if (Validator.isNotNull(entryClassName) && Validator.isNotNull(entryClassPK)) {
                return _toSimpleName(entryClassName) + " #" + entryClassPK;
            }
        }

        return workflowTask.getName();
    }

    private String _getAssigneeName(WorkflowTask workflowTask) {
        long assigneeUserId = workflowTask.getAssigneeUserId();

        if (assigneeUserId > 0) {
            User user = _userLocalService.fetchUser(assigneeUserId);

            if (user != null) {
                return user.getFullName();
            }
        }

        for (WorkflowTaskAssignee workflowTaskAssignee : workflowTask.getWorkflowTaskAssignees()) {

            if (Role.class.getName().equals(
                    workflowTaskAssignee.getAssigneeClassName())) {

                Role role = _roleLocalService.fetchRole(
                        workflowTaskAssignee.getAssigneeClassPK());

                if (role != null) {
                    return "Role: " + role.getName();
                }
            }

            if (User.class.getName().equals(
                    workflowTaskAssignee.getAssigneeClassName())) {

                User user = _userLocalService.fetchUser(
                        workflowTaskAssignee.getAssigneeClassPK());

                if (user != null) {
                    return user.getFullName();
                }
            }
        }

        return "Unassigned";
    }

    private String _getScopeGroupName(long groupId) {
        if (groupId <= 0) {
            return "Global";
        }

        Group group = _groupLocalService.fetchGroup(groupId);

        if (group == null) {
            return String.valueOf(groupId);
        }

        return GetterUtil.getString(group.getGroupKey(), String.valueOf(groupId));
    }

    private WorkflowInstance _getWorkflowInstance(long companyId, long workflowInstanceId)
            throws WorkflowException {

        return _workflowInstanceManager.getWorkflowInstance(
                companyId, workflowInstanceId);
    }

    private boolean _matchesScopeGroup(
            PortalOpsRequestContext context, WorkflowInstance workflowInstance) {

        if ((context.getGroupId() <= 0) || (workflowInstance == null)) {
            return true;
        }

        Map<String, Serializable> workflowContext = workflowInstance.getWorkflowContext();

        long workflowGroupId = GetterUtil.getLong(
                String.valueOf(
                        workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID)));

        return (workflowGroupId <= 0) || (workflowGroupId == context.getGroupId());
    }

    private List<WorkflowPendingSummary> _summarizeByAssignee(
            List<WorkflowTaskSummary> workflowTaskSummaries) {

        return _toPendingSummaries(
                workflowTaskSummaries, true);
    }

    private List<WorkflowPendingSummary> _summarizeByWorkflowDefinition(
            List<WorkflowTaskSummary> workflowTaskSummaries) {

        return _toPendingSummaries(
                workflowTaskSummaries, false);
    }

    private WorkflowTaskSummary _toWorkflowTaskSummary(
            WorkflowTask workflowTask, WorkflowInstance workflowInstance) {

        return new WorkflowTaskSummary(
                _getAssigneeName(workflowTask),
                _getAssetTitle(workflowTask, workflowInstance),
                workflowTask.getWorkflowDefinitionName(),
                workflowTask.getWorkflowDefinitionVersion(),
                workflowTask.getWorkflowInstanceId(),
                workflowTask.getWorkflowTaskId(), workflowTask.getName());
    }

    private List<WorkflowPendingSummary> _toPendingSummaries(
            List<WorkflowTaskSummary> workflowTaskSummaries, boolean groupByAssignee) {

        Map<String, Integer> counts = new LinkedHashMap<>();

        for (WorkflowTaskSummary workflowTaskSummary : workflowTaskSummaries) {
            String key = groupByAssignee ? workflowTaskSummary.getAssigneeName()
                    : workflowTaskSummary.getWorkflowDefinitionLabel();

            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        List<WorkflowPendingSummary> workflowPendingSummaries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            workflowPendingSummaries.add(
                    new WorkflowPendingSummary(
                            entry.getKey(), entry.getKey(), entry.getValue()));
        }

        return workflowPendingSummaries;
    }

    private String _toSimpleName(String className) {
        int index = className.lastIndexOf('.');

        if (index == -1) {
            return className;
        }

        return className.substring(index + 1);
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsWorkflowInspectionServiceComponent.class);

    @Reference
    private GroupLocalService _groupLocalService;

    @Reference
    private RoleLocalService _roleLocalService;

    @Reference
    private UserLocalService _userLocalService;

    @Reference
    private WorkflowInstanceManager _workflowInstanceManager;

    @Reference
    private WorkflowTaskManager _workflowTaskManager;

}