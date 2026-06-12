<%@ include file="/init.jsp" %>

<%
PortalOpsAssistantData assistantData = dashboardData.getAssistantData();
String assistantPageFormId =
    renderResponse.getNamespace() + "portalOpsAssistantPageForm";
String assistantPageInputId =
    renderResponse.getNamespace() + "portalOpsAssistantPagePrompt";
String assistantPageLoadingId =
    renderResponse.getNamespace() + "portalOpsAssistantPageLoading";
%>

<portlet:renderURL var="portalOpsAssistantURL">
  <portlet:param name="screen" value="assistant" />
</portlet:renderURL>

<section class="card portalops-assistant-page">
  <div class="card-body portalops-assistant-page-body">
    <header class="portalops-assistant-page-header">
      <div class="portalops-section-title">PortalOps Assistant</div>
      <p class="portalops-section-subtitle">
        Prompt your configured AI provider directly from PortalOps. The conversation expands to the full work area so longer responses stay readable.
      </p>
    </header>

    <div class="portalops-assistant-conversation" id="<portlet:namespace />portalOpsAssistantConversation">
      <%@ include file="/components/assistant_insight_card.jspf" %>
    </div>

    <div class="portalops-assistant-composer-shell">
      <form action="<%= portalOpsAssistantURL %>" id="<%= HtmlUtil.escapeAttribute(assistantPageFormId) %>" method="get">
        <input name="<portlet:namespace />assistantMode" type="hidden" value="prompt" />
        <input name="<portlet:namespace />screen" type="hidden" value="assistant" />

        <div class="portalops-assistant-prompt-row">
          <input
            class="form-control"
            id="<%= HtmlUtil.escapeAttribute(assistantPageInputId) %>"
            name="<portlet:namespace />assistantPrompt"
            placeholder="<%= HtmlUtil.escapeAttribute(assistantData.getPlaceholder()) %>"
            type="text"
            value="<%= HtmlUtil.escapeAttribute(assistantPromptValue == null ? "" : assistantPromptValue) %>"
          />

          <button class="btn btn-primary portalops-assistant-send" type="submit">
            Send
          </button>
        </div>

        <div class="portalops-assistant-loading" hidden id="<%= HtmlUtil.escapeAttribute(assistantPageLoadingId) %>">
          Sending prompt to the configured AI provider...
        </div>
      </form>

      <%@ include file="/components/suggested_prompt_list.jspf" %>
    </div>
  </div>
</section>

<script>
  (function() {
    var form = document.getElementById('<%= HtmlUtil.escapeJS(assistantPageFormId) %>');
    var conversation = document.getElementById('<%= HtmlUtil.escapeJS(renderResponse.getNamespace()) %>portalOpsAssistantConversation');
    var promptInput = document.getElementById('<%= HtmlUtil.escapeJS(assistantPageInputId) %>');
    var loadingIndicator = document.getElementById('<%= HtmlUtil.escapeJS(assistantPageLoadingId) %>');

    if (!form || !promptInput) {
      return;
    }

    var promptButtons = document.querySelectorAll(
      '[data-portalops-assistant-prompt="<%= HtmlUtil.escapeJS(renderResponse.getNamespace()) %>"]'
    );

    promptButtons.forEach(function(button) {
      button.addEventListener('click', function() {
        promptInput.value = button.getAttribute('data-prompt') || '';
        promptInput.focus();
      });
    });

    if (conversation) {
      conversation.scrollTop = conversation.scrollHeight;
    }

    form.addEventListener('submit', function() {
      var submitButton = form.querySelector('button[type="submit"]');

      if (loadingIndicator) {
        loadingIndicator.hidden = false;
      }

      if (submitButton) {
        submitButton.disabled = true;
      }

      if (conversation) {
        conversation.scrollTop = conversation.scrollHeight;
      }
    });
  })();
</script>
