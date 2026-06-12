package com.portalops.assistant.api.payload;

public class SystemHealthPayload implements AssistantPayload {

    public SystemHealthPayload(
            int activeClusterNodes, int totalClusterNodes,
            boolean databaseHealthy, boolean searchOperational,
            int failedScheduledJobs) {

        _activeClusterNodes = activeClusterNodes;
        _databaseHealthy = databaseHealthy;
        _failedScheduledJobs = failedScheduledJobs;
        _searchOperational = searchOperational;
        _totalClusterNodes = totalClusterNodes;
    }

    public int getActiveClusterNodes() {
        return _activeClusterNodes;
    }

    public int getFailedScheduledJobs() {
        return _failedScheduledJobs;
    }

    public int getTotalClusterNodes() {
        return _totalClusterNodes;
    }

    public boolean isDatabaseHealthy() {
        return _databaseHealthy;
    }

    public boolean isSearchOperational() {
        return _searchOperational;
    }

    private final int _activeClusterNodes;
    private final boolean _databaseHealthy;
    private final int _failedScheduledJobs;
    private final boolean _searchOperational;
    private final int _totalClusterNodes;

}
