package com.portalops.web.internal.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsSystemHealthData implements Serializable {

    public PortalOpsSystemHealthData(
            String portalOpsVersion, String liferayVersion,
            String bundleStatus, int knowledgeCount, int policyCount,
            int workflowCount, int auditCount, int activeServicesCount,
            List<String> bundleNames, List<String> activeServices) {

        _activeServices = Collections.unmodifiableList(
                new ArrayList<>(activeServices));
        _activeServicesCount = activeServicesCount;
        _auditCount = auditCount;
        _bundleNames = Collections.unmodifiableList(new ArrayList<>(bundleNames));
        _bundleStatus = Objects.requireNonNull(bundleStatus);
        _knowledgeCount = knowledgeCount;
        _liferayVersion = Objects.requireNonNull(liferayVersion);
        _policyCount = policyCount;
        _portalOpsVersion = Objects.requireNonNull(portalOpsVersion);
        _workflowCount = workflowCount;
    }

    public List<String> getActiveServices() {
        return _activeServices;
    }

    public int getActiveServicesCount() {
        return _activeServicesCount;
    }

    public int getAuditCount() {
        return _auditCount;
    }

    public List<String> getBundleNames() {
        return _bundleNames;
    }

    public String getBundleStatus() {
        return _bundleStatus;
    }

    public int getKnowledgeCount() {
        return _knowledgeCount;
    }

    public String getLiferayVersion() {
        return _liferayVersion;
    }

    public int getPolicyCount() {
        return _policyCount;
    }

    public String getPortalOpsVersion() {
        return _portalOpsVersion;
    }

    public int getWorkflowCount() {
        return _workflowCount;
    }

    private final List<String> _activeServices;
    private final int _activeServicesCount;
    private final int _auditCount;
    private final List<String> _bundleNames;
    private final String _bundleStatus;
    private final int _knowledgeCount;
    private final String _liferayVersion;
    private final int _policyCount;
    private final String _portalOpsVersion;
    private final int _workflowCount;

}
