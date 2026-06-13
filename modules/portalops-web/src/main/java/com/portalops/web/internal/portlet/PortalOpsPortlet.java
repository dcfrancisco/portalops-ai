package com.portalops.web.internal.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import com.portalops.ai.api.ActionLink;
import com.portalops.ai.api.FindingCard;
import com.portalops.ai.api.PortalOpsAnalysisResponse;
import com.portalops.ai.api.Recommendation;
import com.portalops.assistant.api.AssistantAction;
import com.portalops.assistant.api.AssistantCommand;
import com.portalops.assistant.api.AssistantStatus;
import com.portalops.assistant.api.AssistantCommandRouter;
import com.portalops.assistant.api.PortalOpsAssistantRequest;
import com.portalops.assistant.api.PortalOpsAssistantResponse;
import com.portalops.assistant.api.PortalOpsAssistantService;
import com.portalops.assistant.api.payload.AssistantPayload;
import com.portalops.assistant.api.payload.FailedWorkflowPayload;
import com.portalops.assistant.api.payload.PermissionRiskPayload;
import com.portalops.assistant.api.payload.RecentChangesPayload;
import com.portalops.assistant.api.payload.SearchHealthPayload;
import com.portalops.assistant.api.payload.StaleContentPayload;
import com.portalops.assistant.api.payload.SystemHealthPayload;
import com.portalops.assistant.api.payload.UserFindingsPayload;
import com.portalops.api.audit.AuditRecorder;
import com.portalops.api.knowledge.PortalKnowledgeSnapshot;
import com.portalops.api.policy.CommandAuthorizer;
import com.portalops.api.service.PortalOpsFacade;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.web.internal.constants.PortalOpsPortletKeys;
import com.portalops.web.internal.dashboard.PortalOpsDashboardDataProvider;
import com.portalops.web.internal.display.DataSourceType;
import com.portalops.web.internal.display.PortalOpsAssistantConversationTurn;
import com.portalops.web.internal.display.PortalOpsDashboardCard;
import com.portalops.web.internal.display.PortalOpsDashboardData;
import com.portalops.web.internal.display.PortalOpsDashboardSection;
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
import javax.portlet.PortletSession;
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
        "com.liferay.portlet.system=true",
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
        PortalOpsAnalysisResponse portalOpsAnalysisResponse =
                _getPortalOpsAnalysisResponse(portalOpsAssistantResponse);

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
        portalOpsDashboardData = portalOpsDashboardData.withInsightsSection(
                _toInsightsSection(
                        portalOpsAssistantResponse, portalOpsAnalysisResponse));
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
        renderRequest.setAttribute(
                "PORTALOPS_ASSISTANT_TURNS",
                _getPortalOpsAssistantConversationTurns(
                        renderRequest, portalOpsAssistantResponse));
        renderRequest.setAttribute(
                "PORTALOPS_ANALYSIS_RESPONSE", portalOpsAnalysisResponse);

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
            case "assistant":
                return "PortalOps Assistant";
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

        if ("assistant".equals(activeScreen)) {
            return "Send a prompt to your configured AI provider and work directly in the PortalOps conversation.";
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

        if ("assistant".equals(activeScreen)) {
            return "AI Assistant";
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

        if ("assistant".equals(activeScreen)) {
            return "neutral";
        }

        return "success";
    }

    private PortalOpsAssistantResponse<? extends AssistantPayload>
            _getPortalOpsAssistantResponse(
                    RenderRequest renderRequest,
                    PortalOpsRequestContext portalOpsRequestContext) {

        String assistantMode = ParamUtil.getString(
                renderRequest, "assistantMode");
        String assistantPrompt = ParamUtil.getString(
                renderRequest, "assistantPrompt");

        if ("prompt".equals(assistantMode)) {
            if (assistantPrompt.isEmpty()) {
                return null;
            }

            return _portalOpsAssistantService.chat(
                    assistantPrompt, portalOpsRequestContext);
        }

        if (assistantPrompt.isEmpty()) {
            return null;
        }

        return _portalOpsAssistantService.chat(
                assistantPrompt, portalOpsRequestContext);
    }

    private PortalOpsAnalysisResponse _getPortalOpsAnalysisResponse(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        if (portalOpsAssistantResponse == null) {
            return null;
        }

        return new PortalOpsAnalysisResponse(
                portalOpsAssistantResponse.getSummary(),
                _toFindingCards(portalOpsAssistantResponse),
                _toRecommendations(portalOpsAssistantResponse),
                _toActionLinks(portalOpsAssistantResponse));
    }

    private PortalOpsDashboardSection _toInsightsSection(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse,
            PortalOpsAnalysisResponse portalOpsAnalysisResponse) {

        if ((portalOpsAssistantResponse == null) ||
            (portalOpsAnalysisResponse == null) ||
            portalOpsAnalysisResponse.getFindingCards().isEmpty()) {

            return null;
        }

        return new PortalOpsDashboardSection(
                "insights", "Insights",
                _toInsightsDescription(portalOpsAssistantResponse),
                _toInsightCards(portalOpsAnalysisResponse));
    }

    private List<PortalOpsAssistantConversationTurn>
            _getPortalOpsAssistantConversationTurns(
                    RenderRequest renderRequest,
                    PortalOpsAssistantResponse<? extends AssistantPayload>
                            portalOpsAssistantResponse) {

        PortletSession portletSession = renderRequest.getPortletSession();
        List<PortalOpsAssistantConversationTurn> turns =
                (List<PortalOpsAssistantConversationTurn>)
                        portletSession.getAttribute(
                                _PORTALOPS_ASSISTANT_TURNS,
                                PortletSession.PORTLET_SCOPE);

        if (turns == null) {
            turns = new ArrayList<>();
        }

        String assistantMode = ParamUtil.getString(
                renderRequest, "assistantMode");
        String assistantPrompt = ParamUtil.getString(
                renderRequest, "assistantPrompt");

        if ("prompt".equals(assistantMode) &&
            (portalOpsAssistantResponse != null) &&
            !assistantPrompt.isEmpty()) {

            turns = new ArrayList<>(turns);

            turns.add(
                    new PortalOpsAssistantConversationTurn(
                            assistantPrompt, portalOpsAssistantResponse));

            while (turns.size() > 8) {
                turns.remove(0);
            }

            portletSession.setAttribute(
                    _PORTALOPS_ASSISTANT_TURNS, turns,
                    PortletSession.PORTLET_SCOPE);
        }

        return List.copyOf(turns);
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

    private PortalOpsAssistantResponse<? extends AssistantPayload>
            _getSimulatedAssistantResponse(String assistantPrompt) {

        String normalizedPrompt = assistantPrompt.trim();
        String lowerCasePrompt = normalizedPrompt.toLowerCase(Locale.ROOT);

        if (_containsAny(
                lowerCasePrompt, "search", "index", "reindex")) {

            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.WARNING, "Search Health Analysis",
                    "I reviewed your prompt and simulated a PortalOps search analysis with a few signals worth checking.",
                    List.of(
                            "Search failures are clustered around recent content updates.",
                            "One index freshness signal suggests a delayed synchronization window.",
                            "Zero-result queries are trending higher for marketing content."),
                    List.of(
                            "Review recent indexing jobs before scheduling a full reindex.",
                            "Inspect failed search requests tied to the latest content changes."),
                    List.of(
                            new AssistantAction("Open Search Health", "assistant"),
                            new AssistantAction("Review Recent Changes", "audit")),
                    null);
        }

        if (_containsAny(
                lowerCasePrompt, "stale", "expired", "content", "draft")) {

            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.WARNING, "Content Investigation",
                    "I reviewed your prompt and simulated a content investigation with editorial follow-up items.",
                    List.of(
                            "Multiple content items appear stale relative to their ownership cycle.",
                            "A small group of drafts has been sitting without approval follow-up.",
                            "Several expiring items should be reviewed before the next publishing window."),
                    List.of(
                            "Review stale content first and confirm ownership.",
                            "Clear pending approvals before the next content release."),
                    List.of(
                            new AssistantAction("Review Content", "content"),
                            new AssistantAction("Open Knowledge", "knowledge")),
                    null);
        }

        if (_containsAny(
                lowerCasePrompt, "permission", "permissions", "audit", "risk",
                "security", "access")) {

            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.WARNING, "Permission Risk Review",
                    "I reviewed your prompt and simulated a permission analysis with a few governance concerns.",
                    List.of(
                            "Recent permission changes include elevated access on sensitive resources.",
                            "A few high-impact roles should be reviewed against expected ownership.",
                            "Audit activity suggests configuration-related access changes."),
                    List.of(
                            "Review elevated permissions before the next release window.",
                            "Compare recent access changes with approved governance policies."),
                    List.of(
                            new AssistantAction("Open Audit", "audit"),
                            new AssistantAction("Review Policy", "policy")),
                    null);
        }

        if (_containsAny(
                lowerCasePrompt, "workflow", "approval", "failed", "stuck")) {

            return new PortalOpsAssistantResponse<>(
                    AssistantStatus.WARNING, "Workflow Review",
                    "I reviewed your prompt and simulated a workflow investigation with possible blockage signals.",
                    List.of(
                            "At least one workflow appears to be retrying without resolution.",
                            "Pending approvals are concentrated in a small set of content owners.",
                            "A failed workflow may be impacting scheduled publishing."),
                    List.of(
                            "Review failed workflows first to unblock pending content.",
                            "Confirm approver assignments for delayed workflow queues."),
                    List.of(
                            new AssistantAction("Open Workflow", "workflow"),
                            new AssistantAction("Review Content", "content")),
                    null);
        }

        return new PortalOpsAssistantResponse<>(
                AssistantStatus.INFO, "PortalOps Analysis",
                "I reviewed your prompt and generated a simulated PortalOps response in the assistant panel.",
                List.of(
                        "PortalOps can translate this request into findings across content, search, audit, and workflow domains.",
                        "No live AI provider is configured yet, so this response is mocked for UI validation.",
                        "The current assistant flow is ready to swap to a real provider later."),
                List.of(
                        "Refine the prompt around content, search, permissions, workflows, or recent changes.",
                        "Add an AI provider later without changing the right-rail interaction model."),
                List.of(
                        new AssistantAction("Open Overview", "dashboard"),
                        new AssistantAction("Review System Health", "settings")),
                null);
    }

    private boolean _containsAny(String value, String... candidates) {
        for (String candidate : candidates) {
            if (value.contains(candidate)) {
                return true;
            }
        }

        return false;
    }

    private List<ActionLink> _toActionLinks(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        if (portalOpsAssistantResponse.getPayload() instanceof
                StaleContentPayload) {

            return List.of(
                    new ActionLink(
                            "Review Stale Content", "content", ""));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                PermissionRiskPayload) {

            return List.of(
                    new ActionLink(
                            "Open Permission Risks", "audit", ""));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                SearchHealthPayload) {

            return List.of(
                    new ActionLink(
                            "Open Search Health", "assistant", ""));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                FailedWorkflowPayload) {

            return List.of(
                    new ActionLink(
                            "Open Workflow Review", "workflow", ""));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                RecentChangesPayload) {

            return List.of(
                    new ActionLink(
                            "Show Recent Changes", "audit", ""));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                SystemHealthPayload) {

            return List.of(
                    new ActionLink(
                            "Open System Health", "settings", ""));
        }

        return List.of();
    }

    private List<PortalOpsDashboardCard> _toInsightCards(
            PortalOpsAnalysisResponse portalOpsAnalysisResponse) {

        List<PortalOpsDashboardCard> cards = new ArrayList<>();

        for (FindingCard findingCard : portalOpsAnalysisResponse.getFindingCards()) {
            cards.add(
                    new PortalOpsDashboardCard(
                            findingCard.getTitle(), findingCard.getValue(),
                            findingCard.getStatus(),
                            _toInsightCardContext(findingCard.getStatus()),
                            findingCard.getSummary(), DataSourceType.LIVE));
        }

        return List.copyOf(cards);
    }

    private String _toInsightCardContext(String status) {
        switch (status) {
            case "critical":
                return "Needs attention";
            case "warning":
                return "Review";
            case "success":
                return "Healthy";
            default:
                return "Current";
        }
    }

    private String _toInsightsDescription(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        if (portalOpsAssistantResponse.getPayload() instanceof
                UserFindingsPayload) {

            return "User-related operational context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                SearchHealthPayload) {

            return "Search-related operational context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                StaleContentPayload) {

            return "Content-related operational context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                PermissionRiskPayload) {

            return "Governance-related operational context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                FailedWorkflowPayload) {

            return "Workflow-related operational context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                RecentChangesPayload) {

            return "Recent operational change context from the current assistant response.";
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                SystemHealthPayload) {

            return "System health context from the current assistant response.";
        }

        return "Operational context from the current assistant response.";
    }

    private List<FindingCard> _toFindingCards(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        String status = _toFindingStatus(portalOpsAssistantResponse.getStatus());

        if (portalOpsAssistantResponse.getPayload() instanceof
                SystemHealthPayload) {

            SystemHealthPayload systemHealthPayload =
                    (SystemHealthPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Search Health",
                            systemHealthPayload.isSearchOperational() ?
                                    "Operational" : "Needs Review",
                            status,
                            "Search health is a primary operational signal."),
                    new FindingCard(
                            "Failed Jobs",
                            String.valueOf(
                                    systemHealthPayload.getFailedScheduledJobs()),
                            status,
                            "Failed or stuck jobs need intervention before they cascade."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                UserFindingsPayload) {

            UserFindingsPayload userFindingsPayload =
                    (UserFindingsPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "User Count",
                            String.valueOf(userFindingsPayload.getTotalUsers()),
                            status, "Users in the current portal instance."),
                    new FindingCard(
                            "Active Users",
                            String.valueOf(userFindingsPayload.getActiveUsers()),
                            status, "Users with approved status."),
                    new FindingCard(
                            "Administrator Accounts",
                            String.valueOf(
                                    userFindingsPayload.
                                            getAdministratorAccounts()),
                            status, "Users assigned the Administrator role."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                StaleContentPayload) {

            StaleContentPayload staleContentPayload =
                    (StaleContentPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Stale Content",
                            String.valueOf(
                                    staleContentPayload.getStaleContentItems().size()),
                            status,
                            "Stale content weakens trust and content governance."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                SearchHealthPayload) {

            SearchHealthPayload searchHealthPayload =
                    (SearchHealthPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Search Failures",
                            String.valueOf(
                                    searchHealthPayload.getFailedIndexes()),
                            status,
                            "Search failures directly affect discovery quality."),
                    new FindingCard(
                            "Indexed Documents",
                            String.valueOf(
                                    searchHealthPayload.getIndexedDocuments()),
                            "info",
                            "Indexed document coverage helps explain discovery reach."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                PermissionRiskPayload) {

            PermissionRiskPayload permissionRiskPayload =
                    (PermissionRiskPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Permission Risks",
                            String.valueOf(
                                    permissionRiskPayload.getPermissionRiskItems().size()),
                            status,
                            "Elevated permissions should be reviewed against operational policy."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                FailedWorkflowPayload) {

            FailedWorkflowPayload failedWorkflowPayload =
                    (FailedWorkflowPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Failed Workflows",
                            String.valueOf(
                                    failedWorkflowPayload.getFailedWorkflowItems().size()),
                            status,
                            "Workflow failures block approvals and operational throughput."));
        }

        if (portalOpsAssistantResponse.getPayload() instanceof
                RecentChangesPayload) {

            RecentChangesPayload recentChangesPayload =
                    (RecentChangesPayload)portalOpsAssistantResponse.getPayload();

            return List.of(
                    new FindingCard(
                            "Recent Changes",
                            String.valueOf(
                                    recentChangesPayload.getRecentChangeItems().size()),
                            "info",
                            "Recent changes can explain newly observed incidents."));
        }

        return List.of();
    }

    private List<Recommendation> _toRecommendations(
            PortalOpsAssistantResponse<? extends AssistantPayload>
                    portalOpsAssistantResponse) {

        List<Recommendation> recommendations = new ArrayList<>();

        for (String recommendation :
                portalOpsAssistantResponse.getRecommendations()) {

            recommendations.add(
                    new Recommendation(
                            recommendation, recommendation));
        }

        return recommendations;
    }

    private String _toFindingStatus(AssistantStatus assistantStatus) {
        switch (assistantStatus) {
            case ERROR:
                return "critical";
            case WARNING:
                return "warning";
            case INFO:
                return "info";
            case SUCCESS:
            default:
                return "success";
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsPortlet.class);
    private static final String _PORTALOPS_ASSISTANT_TURNS =
            "PORTALOPS_ASSISTANT_TURNS";

    @Reference
    private PortalOpsFacade _portalOpsFacade;

    @Reference
    private PortalOpsDashboardDataProvider _portalOpsDashboardDataProvider;

    @Reference
    private PortalOpsAssistantService _portalOpsAssistantService;

}
