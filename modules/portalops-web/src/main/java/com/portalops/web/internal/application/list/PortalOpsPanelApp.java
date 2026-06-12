package com.portalops.web.internal.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.portal.kernel.model.Portlet;

import com.portalops.web.internal.constants.PortalOpsPanelCategoryKeys;
import com.portalops.web.internal.constants.PortalOpsPortletKeys;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    property = {
        "panel.app.order:Integer=100",
        "panel.category.key=" +
            PortalOpsPanelCategoryKeys.CONTROL_PANEL_PORTALOPS
    },
    service = PanelApp.class
)
public class PortalOpsPanelApp extends BasePanelApp {

    @Override
    public Portlet getPortlet() {
        return _portlet;
    }

    @Override
    public String getPortletId() {
        return PortalOpsPortletKeys.PORTALOPS;
    }

    @Reference(
        target = "(javax.portlet.name=" + PortalOpsPortletKeys.PORTALOPS + ")"
    )
    private Portlet _portlet;

}
