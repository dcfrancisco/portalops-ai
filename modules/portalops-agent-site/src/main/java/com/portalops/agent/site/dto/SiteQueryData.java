package com.portalops.agent.site.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SiteQueryData implements Serializable {

	public static final String TYPE_PRIVATE_PAGES = "private-pages";
	public static final String TYPE_PUBLIC_PAGES = "public-pages";
	public static final String TYPE_SITE_ACTIVITY = "site-activity";
	public static final String TYPE_SITE_MEMBERSHIP = "site-membership";
	public static final String TYPE_SITE_PAGES = "site-pages";
	public static final String TYPE_SITES = "sites";

	public SiteQueryData(
		int activeSites, long companyId, String domain, int matchedPrivatePages,
		int matchedPublicPages, int matchedSites, String queryType,
		String scope, SiteData siteFilter, List<SiteData> sites,
		int sitesWithoutPages, int totalMemberships, int totalPrivatePages,
		int totalPublicPages, int totalSites) {

		_activeSites = activeSites;
		_companyId = companyId;
		_domain = Objects.requireNonNull(domain);
		_matchedPrivatePages = matchedPrivatePages;
		_matchedPublicPages = matchedPublicPages;
		_matchedSites = matchedSites;
		_queryType = Objects.requireNonNull(queryType);
		_scope = Objects.requireNonNull(scope);
		_siteFilter = siteFilter;
		_sites = List.copyOf(Objects.requireNonNull(sites));
		_sitesWithoutPages = sitesWithoutPages;
		_totalMemberships = totalMemberships;
		_totalPrivatePages = totalPrivatePages;
		_totalPublicPages = totalPublicPages;
		_totalSites = totalSites;
	}

	public int getActiveSites() {
		return _activeSites;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getDomain() {
		return _domain;
	}

	public int getMatchedPrivatePages() {
		return _matchedPrivatePages;
	}

	public int getMatchedPublicPages() {
		return _matchedPublicPages;
	}

	public int getMatchedSites() {
		return _matchedSites;
	}

	public String getQueryType() {
		return _queryType;
	}

	public String getScope() {
		return _scope;
	}

	public SiteData getSiteFilter() {
		return _siteFilter;
	}

	public List<SiteData> getSites() {
		return _sites;
	}

	public int getSitesWithoutPages() {
		return _sitesWithoutPages;
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
	private final long _companyId;
	private final String _domain;
	private final int _matchedPrivatePages;
	private final int _matchedPublicPages;
	private final int _matchedSites;
	private final String _queryType;
	private final String _scope;
	private final SiteData _siteFilter;
	private final List<SiteData> _sites;
	private final int _sitesWithoutPages;
	private final int _totalMemberships;
	private final int _totalPrivatePages;
	private final int _totalPublicPages;
	private final int _totalSites;

}
