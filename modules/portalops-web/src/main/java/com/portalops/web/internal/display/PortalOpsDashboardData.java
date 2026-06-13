package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsDashboardData implements Serializable {

    public PortalOpsDashboardData(
            String headline, String summary, List<PortalOpsDashboardSection> sections,
            PortalOpsDashboardSection insightsSection,
            List<PortalOpsDashboardQuickAction> quickActions,
            PortalOpsAssistantData assistantData) {

        _assistantData = Objects.requireNonNull(assistantData);
        _headline = Objects.requireNonNull(headline);
        _insightsSection = insightsSection;
        _quickActions = Collections.unmodifiableList(
                new ArrayList<>(quickActions));
        _sections = Collections.unmodifiableList(new ArrayList<>(sections));
        _summary = Objects.requireNonNull(summary);
    }

    public PortalOpsAssistantData getAssistantData() {
        return _assistantData;
    }

    public String getHeadline() {
        return _headline;
    }

    public PortalOpsDashboardSection getInsightsSection() {
        return _insightsSection;
    }

    public List<PortalOpsDashboardQuickAction> getQuickActions() {
        return _quickActions;
    }

    public List<PortalOpsDashboardSection> getSections() {
        return _sections;
    }

    public String getSummary() {
        return _summary;
    }

    public PortalOpsDashboardData withInsightsSection(
            PortalOpsDashboardSection insightsSection) {

        return new PortalOpsDashboardData(
                _headline, _summary, _sections, insightsSection, _quickActions,
                _assistantData);
    }

    private final PortalOpsAssistantData _assistantData;
    private final String _headline;
    private final PortalOpsDashboardSection _insightsSection;
    private final List<PortalOpsDashboardQuickAction> _quickActions;
    private final List<PortalOpsDashboardSection> _sections;
    private final String _summary;

}
