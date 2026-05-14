package com.portalops.api.knowledge;

import com.portalops.api.content.ContentSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentKnowledge implements Serializable {

    public ContentKnowledge(List<ContentSummary> staleContent,
            List<ContentSummary> unpublishedDrafts) {

        _staleContent = Collections.unmodifiableList(
                new ArrayList<>(staleContent));
        _unpublishedDrafts = Collections.unmodifiableList(
                new ArrayList<>(unpublishedDrafts));
    }

    public List<ContentSummary> getStaleContent() {
        return _staleContent;
    }

    public List<ContentSummary> getUnpublishedDrafts() {
        return _unpublishedDrafts;
    }

    private final List<ContentSummary> _staleContent;
    private final List<ContentSummary> _unpublishedDrafts;

}