package com.portalops.api.knowledge;

import java.io.Serializable;

public class PortalHealthSummary implements Serializable {

    public PortalHealthSummary(int anomalyCount, int orphanedPageCount,
            int pendingWorkflowCount, int riskyPermissionCount,
            int staleContentCount) {

        _anomalyCount = anomalyCount;
        _orphanedPageCount = orphanedPageCount;
        _pendingWorkflowCount = pendingWorkflowCount;
        _riskyPermissionCount = riskyPermissionCount;
        _staleContentCount = staleContentCount;
    }

    public int getAnomalyCount() {
        return _anomalyCount;
    }

    public int getOrphanedPageCount() {
        return _orphanedPageCount;
    }

    public int getPendingWorkflowCount() {
        return _pendingWorkflowCount;
    }

    public int getRiskyPermissionCount() {
        return _riskyPermissionCount;
    }

    public int getStaleContentCount() {
        return _staleContentCount;
    }

    private final int _anomalyCount;
    private final int _orphanedPageCount;
    private final int _pendingWorkflowCount;
    private final int _riskyPermissionCount;
    private final int _staleContentCount;

}