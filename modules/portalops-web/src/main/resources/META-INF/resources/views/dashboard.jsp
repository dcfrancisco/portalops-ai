<%@ include file="/init.jsp" %>

<div class="portalops-dashboard-shell">
  <% if (!dashboardData.getInsights().isEmpty()) { %>
    <section class="card portalops-insights-panel">
      <div class="card-body">
        <div class="portalops-insights-header">
          <div>
            <div class="portalops-section-title">Insights</div>
            <p class="portalops-section-subtitle">Operational findings that may need follow-up in the workbench.</p>
          </div>
        </div>

        <div class="portalops-insights-list">
          <% for (PortalOpsDashboardInsight insight : dashboardData.getInsights()) {
               javax.portlet.PortletURL insightURL = renderResponse.createRenderURL();
               insightURL.setParameter("screen", insight.getActionScreen());
               String insightHref = insightURL.toString();

               if (!insight.getActionAnchor().isEmpty()) {
                   insightHref = insightHref + "#" + insight.getActionAnchor();
               }
          %>
            <div class="portalops-insight-item">
              <div class="portalops-insight-copy">
                <span class="portalops-status-badge portalops-status-badge-<%= HtmlUtil.escapeAttribute(insight.getSeverity()) %>">
                  <%= HtmlUtil.escape(insight.getSeverity()) %>
                </span>
                <div class="portalops-insight-title"><%= HtmlUtil.escape(insight.getTitle()) %></div>
                <div class="portalops-metric-help"><%= HtmlUtil.escape(insight.getDescription()) %></div>
              </div>
              <a class="btn btn-sm btn-secondary" href="<%= HtmlUtil.escapeAttribute(insightHref) %>">
                <%= HtmlUtil.escape(insight.getActionLabel()) %>
              </a>
            </div>
          <% } %>
        </div>
      </div>
    </section>
  <% } %>

  <% for (PortalOpsDashboardSection section : dashboardData.getSections()) { %>
    <section class="portalops-dashboard-section" id="<%= HtmlUtil.escapeAttribute(section.getId()) %>">
      <div class="portalops-dashboard-section-header">
        <div>
          <div class="portalops-section-title"><%= HtmlUtil.escape(section.getTitle()) %></div>
          <p class="portalops-section-subtitle"><%= HtmlUtil.escape(section.getDescription()) %></p>
        </div>
      </div>

      <div class="portalops-grid portalops-grid-dashboard">
        <% for (PortalOpsDashboardCard card : section.getCards()) {
             request.setAttribute("PORTALOPS_DASHBOARD_CARD", card);
        %>
          <%@ include file="/components/dashboard_card.jspf" %>
        <% } %>
      </div>
    </section>
  <% } %>

  <div class="portalops-grid portalops-grid-secondary portalops-grid-secondary-dashboard">
    <section class="card portalops-actions-panel">
      <div class="card-body">
        <div class="portalops-section-title">Actions</div>
        <p class="portalops-section-subtitle">Recommended operational follow-up steps.</p>

        <div class="portalops-quick-actions">
          <% for (PortalOpsDashboardQuickAction quickAction : dashboardData.getQuickActions()) {
               javax.portlet.PortletURL quickActionURL = renderResponse.createRenderURL();
               quickActionURL.setParameter("screen", quickAction.getScreen());
               String quickActionHref = quickActionURL.toString();

               if (!quickAction.getAnchor().isEmpty()) {
                   quickActionHref = quickActionHref + "#" + quickAction.getAnchor();
               }
          %>
            <a
              class="btn <%= quickAction.isPrimary() ? "btn-primary" : "btn-secondary" %> portalops-quick-action"
              href="<%= HtmlUtil.escapeAttribute(quickActionHref) %>"
            >
              <span class="portalops-quick-action-icon">
                <clay:icon symbol="<%= quickAction.getIcon() %>" />
              </span>
              <span><%= HtmlUtil.escape(quickAction.getLabel()) %></span>
            </a>
          <% } %>
        </div>
      </div>
    </section>
  </div>
</div>
