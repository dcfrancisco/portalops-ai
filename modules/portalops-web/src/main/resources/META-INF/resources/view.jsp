<%@ include file="/init.jsp" %>

<div class="portalops-app">
  <div class="portalops-workbench" id="<portlet:namespace />portalOpsWorkbench">
    <aside class="portalops-workbench-nav">
      <%@ include file="/navigation.jspf" %>
    </aside>

    <section class="portalops-workbench-center">
      <div class="portalops-workbench-controls">
        <button
          aria-expanded="true"
          aria-label="Collapse navigation"
          class="btn btn-monospaced btn-secondary portalops-sidebar-toggle"
          id="<portlet:namespace />portalOpsSidebarToggle"
          type="button"
        >
          <clay:icon symbol="angle-left" />
        </button>
      </div>

      <div class="portalops-workspace-body">
        <% if ("assistant".equals(viewData.getActiveScreen())) { %>
          <jsp:include page="/views/assistant.jsp" />
        <% } else if ("knowledge".equals(viewData.getActiveScreen())) { %>
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
      </div>
    </section>

    <aside class="portalops-workbench-context">
      <%@ include file="/components/assistant_context_panel.jspf" %>
    </aside>
  </div>
</div>

<script>
  (function() {
    var workbench = document.getElementById('<portlet:namespace />portalOpsWorkbench');
    var toggleButton = document.getElementById('<portlet:namespace />portalOpsSidebarToggle');

    if (!workbench || !toggleButton) {
      return;
    }

    toggleButton.addEventListener('click', function() {
      var collapsed = workbench.classList.toggle('portalops-workbench-collapsed');
      toggleButton.setAttribute('aria-expanded', String(!collapsed));
      toggleButton.setAttribute('aria-label', collapsed ? 'Expand navigation' : 'Collapse navigation');
    });
  })();
</script>
