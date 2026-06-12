package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsViewData implements Serializable {

    public PortalOpsViewData(
            String activeScreen, String pageTitle, String pageSubtitle,
            String statusLabel, String statusType,
            List<PortalOpsNavigationItem> navigationItems,
            PortalOpsDashboardData dashboardData,
            PortalOpsSystemHealthData systemHealthData) {

        _activeScreen = Objects.requireNonNull(activeScreen);
        _dashboardData = Objects.requireNonNull(dashboardData);
        _navigationItems = Collections.unmodifiableList(
                new ArrayList<>(navigationItems));
        _pageTitle = Objects.requireNonNull(pageTitle);
        _pageSubtitle = Objects.requireNonNull(pageSubtitle);
        _statusLabel = Objects.requireNonNull(statusLabel);
        _statusType = Objects.requireNonNull(statusType);
        _systemHealthData = Objects.requireNonNull(systemHealthData);
    }

    public String getActiveScreen() {
        return _activeScreen;
    }

    public PortalOpsDashboardData getDashboardData() {
        return _dashboardData;
    }

    public List<PortalOpsNavigationItem> getNavigationItems() {
        return _navigationItems;
    }

    public String getPageTitle() {
        return _pageTitle;
    }

    public String getPageSubtitle() {
        return _pageSubtitle;
    }

    public String getStatusLabel() {
        return _statusLabel;
    }

    public String getStatusType() {
        return _statusType;
    }

    public PortalOpsSystemHealthData getSystemHealthData() {
        return _systemHealthData;
    }

    private final String _activeScreen;
    private final PortalOpsDashboardData _dashboardData;
    private final List<PortalOpsNavigationItem> _navigationItems;
    private final String _pageTitle;
    private final String _pageSubtitle;
    private final String _statusLabel;
    private final String _statusType;
    private final PortalOpsSystemHealthData _systemHealthData;

}
