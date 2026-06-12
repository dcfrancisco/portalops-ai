<%@ include file="/init.jsp" %>

<div class="portalops-assistant-page">
  <section class="card">
    <div class="card-body">
      <div class="portalops-section-title">Investigation</div>
      <p class="portalops-section-subtitle">
        PortalOps Assistant turns investigations into findings, recommendations, and actions.
      </p>
    </div>
  </section>

  <section class="card">
    <div class="card-body">
      <% if (analysisResponse != null) { %>
        <div class="portalops-section-title">Summary</div>
        <p class="portalops-page-subtitle"><%= HtmlUtil.escape(analysisResponse.getSummary()) %></p>

        <div class="portalops-section-title">Findings</div>
        <div class="portalops-grid portalops-grid-analysis">
          <% for (FindingCard findingCard : analysisResponse.getFindingCards()) { %>
            <%
            request.setAttribute("PORTALOPS_FINDING_CARD", findingCard);
            %>
            <%@ include file="/components/finding_card.jspf" %>
          <% } %>
        </div>

        <div class="portalops-section-title">Recommendations</div>
        <div class="portalops-grid portalops-grid-analysis">
          <% for (Recommendation recommendation : analysisResponse.getRecommendations()) { %>
            <%
            request.setAttribute("PORTALOPS_RECOMMENDATION", recommendation);
            %>
            <%@ include file="/components/recommendation_card.jspf" %>
          <% } %>
        </div>

        <div class="portalops-section-title">Actions</div>
        <div class="portalops-grid portalops-grid-analysis">
          <% for (ActionLink actionLink : analysisResponse.getActionLinks()) {
               request.setAttribute("PORTALOPS_ACTION_LINK", actionLink);
          %>
            <%@ include file="/components/action_card.jspf" %>
          <% } %>
        </div>
      <% } else { %>
        <div class="portalops-section-title">Summary</div>
        <p class="portalops-page-subtitle">
          Launch an investigation from the right rail to analyze operational findings in the center workspace.
        </p>

        <div class="portalops-section-title">Suggested Investigations</div>
        <div class="portalops-grid portalops-grid-analysis">
          <div class="portalops-dashboard-card card">
            <div class="card-body">
              <div class="portalops-metric-label">Analyze Portal Health</div>
              <div class="portalops-metric-help">Correlate search, workflow, governance, and system findings.</div>
            </div>
          </div>
          <div class="portalops-dashboard-card card">
            <div class="card-body">
              <div class="portalops-metric-label">Show Stale Content</div>
              <div class="portalops-metric-help">Surface content lifecycle risks and ownership gaps.</div>
            </div>
          </div>
          <div class="portalops-dashboard-card card">
            <div class="card-body">
              <div class="portalops-metric-label">Review Permission Risks</div>
              <div class="portalops-metric-help">Investigate elevated access and policy exposure.</div>
            </div>
          </div>
        </div>
      <% } %>
    </div>
  </section>
</div>
