package com.portalops.assistant.api.payload;

public class SiteFindingsPayload implements AssistantPayload {

    public SiteFindingsPayload(
            int activeSites, int matchedSites, String queryType,
            int totalMemberships, int totalPrivatePages, int totalPublicPages,
            int totalSites) {

        _activeSites = activeSites;
        _matchedSites = matchedSites;
        _queryType = queryType;
        _totalMemberships = totalMemberships;
        _totalPrivatePages = totalPrivatePages;
        _totalPublicPages = totalPublicPages;
        _totalSites = totalSites;
    }

    public int getActiveSites() {
        return _activeSites;
    }

    public int getMatchedSites() {
        return _matchedSites;
    }

    public String getQueryType() {
        return _queryType;
    }

    public int getTotalMemberships() {
        return _totalMemberships;
    }

    public int getTotalPrivatePages() {
        return _totalPrivatePages;
    }

    public int getTotalPublicPages() {
        return _totalPublicPages;
    }

    public int getTotalSites() {
        return _totalSites;
    }

    private final int _activeSites;
    private final int _matchedSites;
    private final String _queryType;
    private final int _totalMemberships;
    private final int _totalPrivatePages;
    private final int _totalPublicPages;
    private final int _totalSites;

}
