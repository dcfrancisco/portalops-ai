package com.portalops.ai.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsAnalysisResponse implements Serializable {

    public PortalOpsAnalysisResponse(
            String summary, List<FindingCard> findingCards,
            List<Recommendation> recommendations,
            List<ActionLink> actionLinks) {

        _actionLinks = Collections.unmodifiableList(new ArrayList<>(actionLinks));
        _findingCards = Collections.unmodifiableList(new ArrayList<>(findingCards));
        _recommendations = Collections.unmodifiableList(
                new ArrayList<>(recommendations));
        _summary = Objects.requireNonNull(summary);
    }

    public List<ActionLink> getActionLinks() {
        return _actionLinks;
    }

    public List<FindingCard> getFindingCards() {
        return _findingCards;
    }

    public List<Recommendation> getRecommendations() {
        return _recommendations;
    }

    public String getSummary() {
        return _summary;
    }

    private final List<ActionLink> _actionLinks;
    private final List<FindingCard> _findingCards;
    private final List<Recommendation> _recommendations;
    private final String _summary;

}
