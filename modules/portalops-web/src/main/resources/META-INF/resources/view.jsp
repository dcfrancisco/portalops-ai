<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.portalops.api.command.PortalOpsCommandResult" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
String currentCommand = (String)request.getAttribute("PORTALOPS_CURRENT_COMMAND");

if (currentCommand == null) {
  currentCommand = "/show workflows pending";
}

PortalOpsCommandResult commandResult =
    (PortalOpsCommandResult)request.getAttribute("PORTALOPS_COMMAND_RESULT");
%>
<div class="portalops-shell">
  <h2>PortalOps AI</h2>
  <p>
    The first MVP workflow slice is active. The command console can now inspect
    pending workflow tasks through supported Liferay workflow APIs.
  </p>

  <portlet:renderURL var="portalOpsCommandURL" />

  <form action="<%= portalOpsCommandURL %>" method="get">
    <input name="<portlet:namespace />runCommand" type="hidden" value="true" />

    <div class="portalops-console-placeholder">
      <label for="portalops-command-input">Command input</label>
      <input
        id="portalops-command-input"
        name="<portlet:namespace />command"
        type="text"
        value="<%= HtmlUtil.escapeAttribute(currentCommand) %>"
      />
      <button type="submit">Run command</button>
    </div>
  </form>

  <h3>Supported MVP command</h3>
  <ul>
    <li>/show workflows pending</li>
  </ul>

  <h3>Result panel</h3>
  <% if (commandResult == null) { %>
    <p>Run the command to inspect pending workflow tasks for the current site scope.</p>
  <% } else { %>
    <p><strong><%= HtmlUtil.escape(commandResult.getTitle()) %></strong></p>
    <p><%= HtmlUtil.escape(commandResult.getSummary()) %></p>

    <ul>
      <% for (String line : commandResult.getLines()) { %>
        <li><%= HtmlUtil.escape(line) %></li>
      <% } %>
    </ul>
  <% } %>
</div>
