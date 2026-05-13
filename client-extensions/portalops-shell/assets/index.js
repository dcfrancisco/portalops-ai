(function () {
  const ELEMENT_NAME = "portalops-shell";

  class PortalOpsShell extends HTMLElement {
    connectedCallback() {
      const backendUrl = this.getAttribute("backend-url") || "/api/commands";

      this.innerHTML = `
				<section class="portalops-shell">
					<header class="portalops-shell__hero">
						<p class="portalops-shell__eyebrow">PortalOps AI</p>
						<h1>Operational intelligence for Liferay 7.4</h1>
						<p class="portalops-shell__summary">
							Frontend shell for command routing, workflow visibility, permission governance,
							content hygiene, and portal management.
						</p>
					</header>

					<div class="portalops-shell__grid">
						<article class="portalops-shell__card">
							<h2>Command Routing</h2>
							<p>Routes slash commands and guided actions to backend capability modules.</p>
							<code>POST ${backendUrl}</code>
						</article>

						<article class="portalops-shell__card">
							<h2>Portal Management</h2>
							<p>Entry point for global portal health, site inventory, and operational dashboards.</p>
						</article>

						<article class="portalops-shell__card">
							<h2>Workflow</h2>
							<p>Focused slice for pending approvals, stalled items, and process visibility.</p>
						</article>

						<article class="portalops-shell__card">
							<h2>Permissions</h2>
							<p>Governance surface for risky grants, publish access, and role-aware review.</p>
						</article>

						<article class="portalops-shell__card">
							<h2>Content</h2>
							<p>Inspection surface for stale drafts, orphaned assets, and editorial hygiene.</p>
						</article>
					</div>

					<div class="portalops-shell__commands">
						<h2>Starter Commands</h2>
						<ul>
							<li>/show workflows pending</li>
							<li>/show permissions risky</li>
							<li>/show stale content</li>
							<li>/show portal health</li>
						</ul>
					</div>
				</section>
			`;
    }
  }

  if (!customElements.get(ELEMENT_NAME)) {
    customElements.define(ELEMENT_NAME, PortalOpsShell);
  }
})();
