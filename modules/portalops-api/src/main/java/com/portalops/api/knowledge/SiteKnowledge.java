package com.portalops.api.knowledge;

import com.portalops.api.site.SiteFinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SiteKnowledge implements Serializable {

    public SiteKnowledge(List<SiteFinding> orphanedPages,
            List<SiteFinding> siteAnomalies) {

        _orphanedPages = Collections.unmodifiableList(
                new ArrayList<>(orphanedPages));
        _siteAnomalies = Collections.unmodifiableList(
                new ArrayList<>(siteAnomalies));
    }

    public List<SiteFinding> getOrphanedPages() {
        return _orphanedPages;
    }

    public List<SiteFinding> getSiteAnomalies() {
        return _siteAnomalies;
    }

    private final List<SiteFinding> _orphanedPages;
    private final List<SiteFinding> _siteAnomalies;

}