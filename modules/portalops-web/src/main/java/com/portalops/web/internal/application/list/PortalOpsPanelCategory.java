package com.portalops.web.internal.application.list;

import com.liferay.application.list.BasePanelCategory;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import com.portalops.web.internal.constants.PortalOpsPanelCategoryKeys;

import org.osgi.service.component.annotations.Component;

@Component(
    property = {
        "panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL,
        "panel.category.order:Integer=250"
    },
    service = PanelCategory.class
)
public class PortalOpsPanelCategory extends BasePanelCategory {

    @Override
    public String getKey() {
        return PortalOpsPanelCategoryKeys.CONTROL_PANEL_PORTALOPS;
    }

    @Override
    public String getLabel(java.util.Locale locale) {
        return "PortalOps";
    }

    @Override
    public boolean isShow(PermissionChecker permissionChecker, Group group) {
        return true;
    }

}
