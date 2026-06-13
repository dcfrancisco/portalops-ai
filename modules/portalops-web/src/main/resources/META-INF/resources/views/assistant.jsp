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

<section class="portalops-assistant-page">
  <div class="portalops-assistant-page-body">
    <div class="portalops-assistant-conversation" id="<portlet:namespace />portalOpsAssistantConversation">
      <%@ include file="/components/assistant_insight_card.jspf" %>
    </div>

    <div class="portalops-assistant-composer-shell">
      <form action="<%= portalOpsAssistantURL %>" id="<%= HtmlUtil.escapeAttribute(assistantPageFormId) %>" method="get">
        <input name="<portlet:namespace />assistantMode" type="hidden" value="prompt" />
        <input name="<portlet:namespace />screen" type="hidden" value="assistant" />

        <div class="portalops-assistant-prompt-row portalops-chat-composer-row">
          <textarea
            class="form-control portalops-chat-input"
            id="<%= HtmlUtil.escapeAttribute(assistantPageInputId) %>"
            name="<portlet:namespace />assistantPrompt"
            placeholder="<%= HtmlUtil.escapeAttribute(assistantData.getPlaceholder()) %>"
            rows="1"
          ><%= HtmlUtil.escape(assistantResponse == null && assistantPromptValue != null ? assistantPromptValue : "") %></textarea>

          <button aria-label="Send prompt" class="btn btn-primary portalops-assistant-send" type="submit">
            <span aria-hidden="true">↑</span>
          </button>
        </div>

        <div class="portalops-assistant-loading" hidden id="<%= HtmlUtil.escapeAttribute(assistantPageLoadingId) %>">
          Sending prompt to the configured AI provider...
        </div>
      </form>

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

    function resizePromptInput() {
      promptInput.style.height = 'auto';
      promptInput.style.height = Math.min(promptInput.scrollHeight, 160) + 'px';
    }

    promptInput.addEventListener('input', resizePromptInput);
    promptInput.addEventListener('keydown', function(event) {
      if ((event.key === 'Enter') && !event.shiftKey) {
        event.preventDefault();

        if (promptInput.value.trim()) {
          form.requestSubmit();
        }
      }
    });

    resizePromptInput();

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
