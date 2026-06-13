<%@ include file="/init.jsp" %>

<div class="portalops-app">
  <div class="portalops-workbench" id="<portlet:namespace />portalOpsWorkbench">
    <aside class="portalops-workbench-nav">
      <%@ include file="/navigation.jspf" %>
    </aside>

    <section class="portalops-workbench-center">
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
    var namespace = '<%= HtmlUtil.escapeJS(renderResponse.getNamespace()) %>';
    var navStorageKey = namespace + 'portalops-nav-collapsed';
    var contextStorageKey = namespace + 'portalops-context-collapsed';

    if (!workbench) {
      return;
    }

    function getDefaultCollapsedState() {
      var width = window.innerWidth || document.documentElement.clientWidth || 0;

      if ((width >= 1024) && (width <= 1365)) {
        return {
          context: true,
          nav: true
        };
      }

      return {
        context: false,
        nav: false
      };
    }

    function readStoredState(storageKey, fallback) {
      try {
        var value = window.localStorage.getItem(storageKey);

        if (value === null) {
          return fallback;
        }

        return value === 'true';
      }
      catch (error) {
        return fallback;
      }
    }

    function writeStoredState(storageKey, collapsed) {
      try {
        window.localStorage.setItem(storageKey, String(collapsed));
      }
      catch (error) {
      }
    }

    function updateWorkbenchState() {
      var navCollapsed = workbench.classList.contains('portalops-nav-collapsed');
      var contextCollapsed = workbench.classList.contains('portalops-context-collapsed');

      workbench.querySelectorAll('[data-portalops-toggle="nav"]').forEach(function(button) {
        button.setAttribute('aria-expanded', String(!navCollapsed));
        button.setAttribute(
          'aria-label', navCollapsed ? 'Expand navigation' : 'Collapse navigation'
        );
      });

      workbench.querySelectorAll('[data-portalops-toggle="context"]').forEach(function(button) {
        button.setAttribute('aria-expanded', String(!contextCollapsed));
        button.setAttribute('aria-label', 'Assistant');
      });
    }

    function setCollapsedState(type, collapsed) {
      var className = type === 'nav' ? 'portalops-nav-collapsed' : 'portalops-context-collapsed';
      var storageKey = type === 'nav' ? navStorageKey : contextStorageKey;

      workbench.classList.toggle(className, collapsed);
      writeStoredState(storageKey, collapsed);
      updateWorkbenchState();
    }

    var defaultState = getDefaultCollapsedState();

    workbench.classList.toggle(
      'portalops-nav-collapsed',
      readStoredState(navStorageKey, defaultState.nav)
    );
    workbench.classList.toggle(
      'portalops-context-collapsed',
      readStoredState(contextStorageKey, defaultState.context)
    );

    updateWorkbenchState();

    workbench.addEventListener('click', function(event) {
      var toggleButton = event.target.closest('[data-portalops-toggle]');

      if (!toggleButton) {
        return;
      }

      var type = toggleButton.getAttribute('data-portalops-toggle');
      var collapsed = type === 'nav' ?
        workbench.classList.contains('portalops-nav-collapsed') :
        workbench.classList.contains('portalops-context-collapsed');

      setCollapsedState(type, !collapsed);
    });
  })();
</script>
