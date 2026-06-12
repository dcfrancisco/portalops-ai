<%@ include file="/init.jsp" %>

<div class="portalops-grid">
  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">PortalOps Version</div>
      <div class="portalops-metric-value"><%= HtmlUtil.escape(systemHealthData.getPortalOpsVersion()) %></div>
      <div class="portalops-metric-help">Current `portalops-web` bundle version.</div>
    </div>
  </div>

  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">Bundle Status</div>
      <div class="portalops-metric-value"><%= HtmlUtil.escape(systemHealthData.getBundleStatus()) %></div>
      <div class="portalops-metric-help">PortalOps bundles active in the runtime.</div>
    </div>
  </div>

  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">Knowledge Signals</div>
      <div class="portalops-metric-value"><%= systemHealthData.getKnowledgeCount() %></div>
      <div class="portalops-metric-help">Knowledge items currently surfaced by PortalOps services.</div>
    </div>
  </div>

  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">Policy Services</div>
      <div class="portalops-metric-value"><%= systemHealthData.getPolicyCount() %></div>
      <div class="portalops-metric-help">Registered PortalOps command policy services.</div>
    </div>
  </div>

  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">Workflow Tasks</div>
      <div class="portalops-metric-value"><%= systemHealthData.getWorkflowCount() %></div>
      <div class="portalops-metric-help">Pending workflow tasks in the current site scope.</div>
    </div>
  </div>

  <div class="portalops-metric-card card">
    <div class="card-body">
      <div class="portalops-metric-label">Audit Services</div>
      <div class="portalops-metric-value"><%= systemHealthData.getAuditCount() %></div>
      <div class="portalops-metric-help">Active PortalOps audit recorder services.</div>
    </div>
  </div>
</div>

<div class="portalops-grid portalops-grid-secondary">
  <div class="card">
    <div class="card-body">
      <div class="portalops-section-title">System Health Diagnostics</div>
      <p class="portalops-section-subtitle">Developer-centric diagnostics live here instead of the main operational dashboard.</p>

      <dl class="portalops-definition-list">
        <dt>Liferay Version</dt>
        <dd><%= HtmlUtil.escape(systemHealthData.getLiferayVersion()) %></dd>
        <dt>Active Bundles</dt>
        <dd><%= systemHealthData.getBundleNames().size() %></dd>
        <dt>Active Services</dt>
        <dd><%= systemHealthData.getActiveServicesCount() %></dd>
      </dl>
    </div>
  </div>

  <div class="card">
    <div class="card-body">
      <div class="portalops-section-title">Active PortalOps Bundles</div>

      <ul class="portalops-list">
        <% for (String bundleName : systemHealthData.getBundleNames()) { %>
          <li><%= HtmlUtil.escape(bundleName) %></li>
        <% } %>
      </ul>
    </div>
  </div>

  <div class="card">
    <div class="card-body">
      <div class="portalops-section-title">Active PortalOps Services</div>

      <ul class="portalops-list portalops-list-tight">
        <% for (String serviceName : systemHealthData.getActiveServices()) { %>
          <li><%= HtmlUtil.escape(serviceName) %></li>
        <% } %>
      </ul>
    </div>
  </div>
</div>
