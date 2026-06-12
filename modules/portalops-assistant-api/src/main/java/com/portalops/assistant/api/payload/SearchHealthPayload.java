package com.portalops.assistant.api.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchHealthPayload implements AssistantPayload {

    public SearchHealthPayload(
            int indexedDocuments, int failedIndexes, String lastReindex,
            List<SearchIssue> searchIssues) {

        _failedIndexes = failedIndexes;
        _indexedDocuments = indexedDocuments;
        _lastReindex = lastReindex;
        _searchIssues = Collections.unmodifiableList(new ArrayList<>(searchIssues));
    }

    public int getFailedIndexes() {
        return _failedIndexes;
    }

    public int getIndexedDocuments() {
        return _indexedDocuments;
    }

    public String getLastReindex() {
        return _lastReindex;
    }

    public List<SearchIssue> getSearchIssues() {
        return _searchIssues;
    }

    private final int _failedIndexes;
    private final int _indexedDocuments;
    private final String _lastReindex;
    private final List<SearchIssue> _searchIssues;

}
