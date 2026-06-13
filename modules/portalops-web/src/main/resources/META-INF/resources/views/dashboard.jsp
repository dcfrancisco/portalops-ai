<%@ include file="/init.jsp" %>

<div class="portalops-dashboard-shell">
  <% if ((dashboardData.getInsightsSection() != null) && !dashboardData.getInsightsSection().getCards().isEmpty()) { %>
    <section class="card portalops-insights-panel">
      <div class="card-body">
        <div class="portalops-insights-header">
          <div>
            <div class="portalops-section-title">Insights</div>
            <p class="portalops-section-subtitle"><%= HtmlUtil.escape(dashboardData.getInsightsSection().getDescription()) %></p>
          </div>
        </div>

        <div class="portalops-grid portalops-grid-dashboard">
          <% for (PortalOpsDashboardCard card : dashboardData.getInsightsSection().getCards()) {
               request.setAttribute("PORTALOPS_DASHBOARD_CARD", card);
          %>
            <%@ include file="/components/dashboard_card.jspf" %>
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
