package com.portalops.web.internal.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantCommandRouter;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.payload.AssistantPayload;
import com.portalops.api.audit.AuditRecorder;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.policy.CommandAuthorizer;
import com.portalops.api.service.PortalOpsFacade;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.web.internal.constants.PortalOpsPortletKeys;
import com.portalops.web.internal.dashboard.PortalOpsDashboardDataProvider;
import com.portalops.web.internal.display.PortalOpsDashboardData;
import com.portalops.web.internal.display.PortalOpsNavigationItem;
import com.portalops.web.internal.display.PortalOpsSystemHealthData;
import com.portalops.web.internal.display.PortalOpsViewData;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    property = {
        "com.liferay.portlet.css-class-wrapper=portalops-web",
        "com.liferay.portlet.display-category=category.hidden",
        "com.liferay.portlet.header-portlet-css=/css/main.css",
        "com.liferay.portlet.instanceable=false",
        "javax.portlet.display-name=PortalOps Dashboard",
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + PortalOpsPortletKeys.PORTALOPS,
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=administrator,power-user,user"
    },
    service = Portlet.class
)
public class PortalOpsPortlet extends MVCPortlet {

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        String activeScreen = ParamUtil.getString(
                renderRequest, "screen", "dashboard");

        ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
                WebKeys.THEME_DISPLAY);

        PortalOpsRequestContext portalOpsRequestContext =
                new PortalOpsRequestContext(
                        themeDisplay.getCompanyId(),
                        themeDisplay.getScopeGroupId(),
                        themeDisplay.getUserId());

        PortalKnowledgeSnapshot portalKnowledgeSnapshot =
                _portalOpsFacade.getPortalKnowledgeSnapshot(
                        portalOpsRequestContext);
        PortalOpsAssistantResponse<? extends AssistantPayload>
                portalOpsAssistantResponse = _getPortalOpsAssistantResponse(
                        renderRequest, portalOpsRequestContext);

        BundleContext bundleContext = FrameworkUtil.getBundle(
                PortalOpsPortlet.class
        ).getBundleContext();

        List<String> activeBundleNames = _getActivePortalOpsBundleNames(
                bundleContext);
        List<String> activeServiceNames = _getActivePortalOpsServiceNames(
                bundleContext);
        PortalOpsDashboardData portalOpsDashboardData =
                _portalOpsDashboardDataProvider.getPortalOpsDashboardData(
                        portalOpsRequestContext, portalKnowledgeSnapshot);
        PortalOpsSystemHealthData portalOpsSystemHealthData =
                new PortalOpsSystemHealthData(
                        FrameworkUtil.getBundle(PortalOpsPortlet.class).
                                getVersion().toString(),
                        ReleaseInfo.getReleaseInfo(),
                        activeBundleNames.size() + " / " +
                                _getPortalOpsBundleCount(bundleContext) + " active",
                        _getKnowledgeCount(portalKnowledgeSnapshot),
                        _getServiceCount(bundleContext, CommandAuthorizer.class),
                        portalKnowledgeSnapshot.getWorkflowKnowledge().
                                getPendingWorkflowInspectionResult().
                                getPendingTaskCount(),
                        _getServiceCount(bundleContext, AuditRecorder.class),
                        activeServiceNames.size(), activeBundleNames,
                        activeServiceNames);

        PortalOpsViewData portalOpsViewData = new PortalOpsViewData(
                activeScreen, _getPageTitle(activeScreen),
                _getPageSubtitle(activeScreen, portalOpsDashboardData),
                _getStatusLabel(activeScreen, portalOpsDashboardData,
                        portalOpsSystemHealthData),
                _getStatusType(activeScreen, portalOpsDashboardData),
                _getNavigationItems(), portalOpsDashboardData,
                portalOpsSystemHealthData);

        renderRequest.setAttribute(
                "PORTALOPS_VIEW_DATA", portalOpsViewData);
        renderRequest.setAttribute(
                "PORTALOPS_ASSISTANT_PROMPT",
                ParamUtil.getString(renderRequest, "assistantPrompt"));
        renderRequest.setAttribute(
                "PORTALOPS_ASSISTANT_RESPONSE", portalOpsAssistantResponse);

        renderResponse.setTitle(portalOpsViewData.getPageTitle());

        super.doView(renderRequest, renderResponse);
    }

    private List<String> _getActivePortalOpsBundleNames(
            BundleContext bundleContext) {

        List<String> bundleNames = new ArrayList<>();

        for (Bundle bundle : bundleContext.getBundles()) {
            if (_isPortalOpsBundle(bundle) && (bundle.getState() == Bundle.ACTIVE)) {
                bundleNames.add(bundle.getSymbolicName());
            }
        }

        bundleNames.sort(String::compareTo);

        return bundleNames;
    }

    private List<String> _getActivePortalOpsServiceNames(
            BundleContext bundleContext) {

        Set<String> serviceNames = new LinkedHashSet<>();

        try {
            ServiceReference<?>[] serviceReferences =
                    bundleContext.getAllServiceReferences(null, null);

            if (serviceReferences == null) {
                return List.of();
            }

            for (ServiceReference<?> serviceReference : serviceReferences) {
                Bundle bundle = serviceReference.getBundle();

                if ((bundle == null) || !_isPortalOpsBundle(bundle) ||
                    (bundle.getState() != Bundle.ACTIVE)) {

                    continue;
                }

                String[] objectClasses = (String[])serviceReference.getProperty(
                        Constants.OBJECTCLASS);

                if (objectClasses == null) {
                    continue;
                }

                for (String objectClass : objectClasses) {
                    serviceNames.add(
                            bundle.getSymbolicName() + " :: " + objectClass);
                }
            }
        }
        catch (InvalidSyntaxException invalidSyntaxException) {
            _log.error(
                    "Unable to read active PortalOps services",
                    invalidSyntaxException);
        }

        List<String> sortedServiceNames = new ArrayList<>(serviceNames);

        sortedServiceNames.sort(Comparator.naturalOrder());

        return sortedServiceNames;
    }

    private int _getKnowledgeCount(
            PortalKnowledgeSnapshot portalKnowledgeSnapshot) {

        return portalKnowledgeSnapshot.getContentKnowledge().getStaleContent().size() +
                portalKnowledgeSnapshot.getContentKnowledge().
                        getUnpublishedDrafts().size() +
                portalKnowledgeSnapshot.getPermissionKnowledge().
                        getHomepagePublishers().size() +
                portalKnowledgeSnapshot.getPermissionKnowledge().
                        getRiskyPermissions().size() +
                portalKnowledgeSnapshot.getSiteKnowledge().getOrphanedPages().size() +
                portalKnowledgeSnapshot.getSiteKnowledge().getSiteAnomalies().size() +
                portalKnowledgeSnapshot.getWorkflowKnowledge().
                        getPendingWorkflowInspectionResult().getPendingTaskCount() +
                portalKnowledgeSnapshot.getWorkflowKnowledge().getStuckWorkflows().size();
    }

    private List<PortalOpsNavigationItem> _getNavigationItems() {
        return List.of(
                new PortalOpsNavigationItem("home", "Dashboard", "dashboard"),
                new PortalOpsNavigationItem(
                        "document-text", "Knowledge", "knowledge"),
                new PortalOpsNavigationItem("lock", "Policy", "policy"),
                new PortalOpsNavigationItem("web-content", "Content", "content"),
                new PortalOpsNavigationItem("forms", "Workflow", "workflow"),
                new PortalOpsNavigationItem("list", "Audit", "audit"),
                new PortalOpsNavigationItem(
                        "analytics", "System Health", "settings"));
    }

    private String _getPageTitle(String activeScreen) {
        switch (activeScreen) {
            case "knowledge":
                return "PortalOps Knowledge";
            case "policy":
                return "PortalOps Policy";
            case "content":
                return "PortalOps Content";
            case "workflow":
                return "PortalOps Workflow";
            case "audit":
                return "PortalOps Audit";
            case "settings":
                return "PortalOps System Health";
            case "dashboard":
            default:
                return "PortalOps Dashboard";
        }
    }

    private String _getPageSubtitle(
            String activeScreen, PortalOpsDashboardData portalOpsDashboardData) {

        if ("dashboard".equals(activeScreen)) {
            return portalOpsDashboardData.getSummary();
        }

        if ("settings".equals(activeScreen)) {
            return "Developer-centric runtime diagnostics are available here " +
                    "so the main dashboard can stay focused on operations.";
        }

        return "PortalOps helps administrators focus on operational risk, " +
                "content readiness, and governance tasks.";
    }

    private String _getStatusLabel(
            String activeScreen, PortalOpsDashboardData portalOpsDashboardData,
            PortalOpsSystemHealthData portalOpsSystemHealthData) {

        if ("settings".equals(activeScreen)) {
            return portalOpsSystemHealthData.getBundleStatus();
        }

        return portalOpsDashboardData.getHeadline();
    }

    private String _getStatusType(
            String activeScreen, PortalOpsDashboardData portalOpsDashboardData) {

        if ("dashboard".equals(activeScreen)) {
            String headline = portalOpsDashboardData.getSections().get(
                    0).getCards().get(0).getStatus();

            if ("critical".equals(headline)) {
                return "critical";
            }

            if ("warning".equals(headline)) {
                return "warning";
            }
        }

        return "success";
    }

    private PortalOpsAssistantResponse<? extends AssistantPayload>
            _getPortalOpsAssistantResponse(
                    RenderRequest renderRequest,
                    PortalOpsRequestContext portalOpsRequestContext) {

        AssistantCommand assistantCommand = _resolveAssistantCommand(
                renderRequest);

        if (assistantCommand == null) {
            return null;
        }

        return _assistantCommandRouter.route(
                new PortalOpsAssistantRequest(
                        assistantCommand,
                        Map.of(
                                "prompt",
                                ParamUtil.getString(
                                        renderRequest, "assistantPrompt")),
                        portalOpsRequestContext));
    }

    private int _getPortalOpsBundleCount(BundleContext bundleContext) {
        int count = 0;

        for (Bundle bundle : bundleContext.getBundles()) {
            if (_isPortalOpsBundle(bundle)) {
                count++;
            }
        }

        return count;
    }

    private <T> int _getServiceCount(
            BundleContext bundleContext, Class<T> clazz) {

        try {
            return bundleContext.getServiceReferences(clazz, null).size();
        }
        catch (InvalidSyntaxException invalidSyntaxException) {
            _log.error(
                    "Unable to count services for " + clazz.getName(),
                    invalidSyntaxException);

            return 0;
        }
    }

    private boolean _isPortalOpsBundle(Bundle bundle) {
        return bundle.getSymbolicName().startsWith("com.portalops.");
    }

    private AssistantCommand _resolveAssistantCommand(
            RenderRequest renderRequest) {

        String assistantCommandValue = ParamUtil.getString(
                renderRequest, "assistantCommand");

        if (!assistantCommandValue.isEmpty()) {
            try {
                return AssistantCommand.valueOf(assistantCommandValue);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                _log.warn(
                        "Unsupported assistant command: " +
                                assistantCommandValue);
            }
        }

        String assistantPrompt = ParamUtil.getString(
                renderRequest, "assistantPrompt");

        if (assistantPrompt.isEmpty()) {
            return null;
        }

        switch (assistantPrompt.trim().toLowerCase(Locale.ROOT)) {
            case "show system health":
                return AssistantCommand.SHOW_SYSTEM_HEALTH;
            case "show stale content":
                return AssistantCommand.SHOW_STALE_CONTENT;
            case "analyze search health":
                return AssistantCommand.ANALYZE_SEARCH_HEALTH;
            case "review permission risks":
                return AssistantCommand.REVIEW_PERMISSION_RISKS;
            case "show recent changes":
                return AssistantCommand.SHOW_RECENT_CHANGES;
            case "show failed workflows":
                return AssistantCommand.SHOW_FAILED_WORKFLOWS;
            default:
                return null;
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsPortlet.class);

    @Reference
    private PortalOpsFacade _portalOpsFacade;

    @Reference
    private PortalOpsDashboardDataProvider _portalOpsDashboardDataProvider;

    @Reference
    private AssistantCommandRouter _assistantCommandRouter;

}
