package com.portalops.web.internal.portlet;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import com.portalops.api.command.PortalOpsCommandRequest;
import com.portalops.api.command.PortalOpsCommandResult;
import com.portalops.api.service.PortalOpsFacade;
import com.portalops.api.service.PortalOpsRequestContext;
import com.portalops.web.internal.constants.PortalOpsPortletKeys;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = {
        "com.liferay.portlet.css-class-wrapper=portalops-web",
        "com.liferay.portlet.display-category=category.sample",
        "com.liferay.portlet.instanceable=true",
        "javax.portlet.display-name=PortalOps AI",
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + PortalOpsPortletKeys.PORTALOPS,
        "javax.portlet.security-role-ref=power-user,user"
}, service = Portlet.class)
public class PortalOpsPortlet extends MVCPortlet {

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        String command = ParamUtil.getString(
                renderRequest, "command", "/show workflows pending");

        renderRequest.setAttribute("PORTALOPS_CURRENT_COMMAND", command);

        if (ParamUtil.getBoolean(renderRequest, "runCommand")) {
            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(
                    WebKeys.THEME_DISPLAY);

            PortalOpsCommandResult commandResult = _portalOpsFacade.execute(
                    new PortalOpsCommandRequest(
                            new PortalOpsRequestContext(
                                    themeDisplay.getCompanyId(),
                                    themeDisplay.getScopeGroupId(),
                                    themeDisplay.getUserId()),
                            command));

            renderRequest.setAttribute(
                    "PORTALOPS_COMMAND_RESULT", commandResult);
        }

        super.doView(renderRequest, renderResponse);
    }

    @Reference
    private PortalOpsFacade _portalOpsFacade;

}