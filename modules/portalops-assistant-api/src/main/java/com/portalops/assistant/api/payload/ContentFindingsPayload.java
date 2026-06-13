package com.portalops.assistant.api.payload;

public class ContentFindingsPayload implements AssistantPayload {

    public ContentFindingsPayload(
            int expiredContent, int matchedContent, int pendingContent,
            String queryType, int totalContent) {

        _expiredContent = expiredContent;
        _matchedContent = matchedContent;
        _pendingContent = pendingContent;
        _queryType = queryType;
        _totalContent = totalContent;
    }

    public int getExpiredContent() {
        return _expiredContent;
    }

    public int getMatchedContent() {
        return _matchedContent;
    }

    public int getPendingContent() {
        return _pendingContent;
    }

    public String getQueryType() {
        return _queryType;
    }

    public int getTotalContent() {
        return _totalContent;
    }

    private final int _expiredContent;
    private final int _matchedContent;
    private final int _pendingContent;
    private final String _queryType;
    private final int _totalContent;

}
