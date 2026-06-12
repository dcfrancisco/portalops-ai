<%@ include file="/init.jsp" %>

<div class="portalops-app">
  <div class="portalops-header">
    <div>
      <div class="portalops-eyebrow">Control Panel Application</div>
      <h1 class="portalops-page-title"><%= HtmlUtil.escape(viewData.getPageTitle()) %></h1>
      <p class="portalops-page-subtitle">
        <%= HtmlUtil.escape(viewData.getPageSubtitle()) %>
      </p>
    </div>
    <div class="portalops-status-chip portalops-status-chip-<%= HtmlUtil.escapeAttribute(viewData.getStatusType()) %>">
      <clay:icon symbol="check-circle" />
      <span><%= HtmlUtil.escape(viewData.getStatusLabel()) %></span>
    </div>
  </div>

  <div class="portalops-layout">
    <aside class="portalops-layout-sidebar">
      <%@ include file="/navigation.jspf" %>
    </aside>

    <section class="portalops-layout-content">
      <% if ("knowledge".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/knowledge.jsp" />
      <% } else if ("policy".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/policy.jsp" />
      <% } else if ("content".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/content.jsp" />
      <% } else if ("workflow".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/workflow.jsp" />
      <% } else if ("audit".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/audit.jsp" />
      <% } else if ("settings".equals(viewData.getActiveScreen())) { %>
        <jsp:include page="/views/settings.jsp" />
      <% } else { %>
        <jsp:include page="/views/dashboard.jsp" />
      <% } %>
    </section>
  </div>
</div>
