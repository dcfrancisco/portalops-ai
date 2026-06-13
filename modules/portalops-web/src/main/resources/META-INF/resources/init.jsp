<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.portalops.ai.api.ActionLink" %>
<%@ page import="com.portalops.ai.api.FindingCard" %>
<%@ page import="com.portalops.ai.api.PortalOpsAnalysisResponse" %>
<%@ page import="com.portalops.ai.api.Recommendation" %>
<%@ page import="com.portalops.assistant.api.AssistantAction" %>
<%@ page import="com.portalops.assistant.api.AssistantStatus" %>
<%@ page import="com.portalops.assistant.api.PortalOpsAssistantResponse" %>
<%@ page import="com.portalops.assistant.api.payload.AssistantPayload" %>
<%@ page import="com.portalops.assistant.api.payload.ContentFindingsPayload" %>
<%@ page import="com.portalops.assistant.api.payload.FailedWorkflowItem" %>
<%@ page import="com.portalops.assistant.api.payload.FailedWorkflowPayload" %>
<%@ page import="com.portalops.assistant.api.payload.PermissionRiskItem" %>
<%@ page import="com.portalops.assistant.api.payload.PermissionRiskPayload" %>
<%@ page import="com.portalops.assistant.api.payload.RecentChangeItem" %>
<%@ page import="com.portalops.assistant.api.payload.RecentChangesPayload" %>
<%@ page import="com.portalops.assistant.api.payload.SearchHealthPayload" %>
<%@ page import="com.portalops.assistant.api.payload.SearchIssue" %>
<%@ page import="com.portalops.assistant.api.payload.SiteFindingsPayload" %>
<%@ page import="com.portalops.assistant.api.payload.StaleContentItem" %>
<%@ page import="com.portalops.assistant.api.payload.StaleContentPayload" %>
<%@ page import="com.portalops.assistant.api.payload.SystemHealthPayload" %>
<%@ page import="com.portalops.assistant.api.payload.UserFindingsPayload" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsAssistantData" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsAssistantConversationTurn" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsAssistantInsight" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsDashboardCard" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsDashboardData" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsDashboardQuickAction" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsDashboardSection" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsNavigationItem" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsSystemHealthData" %>
<%@ page import="com.portalops.web.internal.display.PortalOpsViewData" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/clay" prefix="clay" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<%
PortalOpsViewData viewData =
    (PortalOpsViewData)request.getAttribute("PORTALOPS_VIEW_DATA");
PortalOpsDashboardData dashboardData = viewData.getDashboardData();
PortalOpsSystemHealthData systemHealthData = viewData.getSystemHealthData();
PortalOpsAssistantResponse<? extends AssistantPayload> assistantResponse =
    (PortalOpsAssistantResponse<? extends AssistantPayload>)request.getAttribute(
        "PORTALOPS_ASSISTANT_RESPONSE");
String assistantPromptValue =
    (String)request.getAttribute("PORTALOPS_ASSISTANT_PROMPT");
PortalOpsAnalysisResponse analysisResponse =
    (PortalOpsAnalysisResponse)request.getAttribute(
        "PORTALOPS_ANALYSIS_RESPONSE");
List<PortalOpsAssistantConversationTurn> assistantTurns =
    (List<PortalOpsAssistantConversationTurn>)request.getAttribute(
        "PORTALOPS_ASSISTANT_TURNS");
%>
