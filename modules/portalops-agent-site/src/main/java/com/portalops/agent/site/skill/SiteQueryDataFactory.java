package com.portalops.agent.site.skill;

import com.portalops.agent.site.dto.SiteData;
import com.portalops.agent.site.dto.SiteQueryData;
import com.portalops.agent.site.dto.SitesData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SiteQueryDataFactory {

	public static SiteQueryData create(String prompt, SitesData sitesData) {
		String queryType = _getQueryType(prompt);
		SiteData siteFilter = _findSite(prompt, sitesData.getSites());
		List<SiteData> matchedSites = _getMatchedSites(
			queryType, siteFilter, sitesData.getSites());

		return new SiteQueryData(
			(int)sitesData.getSites(
			).stream(
			).filter(
				SiteData::isActive
			).count(),
			sitesData.getCompanyId(), "sites",
			matchedSites.stream(
			).mapToInt(
				SiteData::getPrivatePageCount
			).sum(),
			matchedSites.stream(
			).mapToInt(
				SiteData::getPublicPageCount
			).sum(),
			matchedSites.size(), queryType, "currentCompany", siteFilter,
			matchedSites,
			(int)sitesData.getSites(
			).stream(
			).filter(
				site -> (site.getPublicPageCount() + site.getPrivatePageCount()) == 0
			).count(),
			sitesData.getSites(
			).stream(
			).mapToInt(
				SiteData::getUserCount
			).sum(),
			sitesData.getSites(
			).stream(
			).mapToInt(
				SiteData::getPrivatePageCount
			).sum(),
			sitesData.getSites(
			).stream(
			).mapToInt(
				SiteData::getPublicPageCount
			).sum(),
			sitesData.getSites().size());
	}

	private static SiteData _findSite(String prompt, List<SiteData> sites) {
		String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);
		SiteData matchedSite = null;

		for (SiteData site : sites) {
			if (_matches(normalizedPrompt, site)) {
				if ((matchedSite == null) ||
					(site.getName().length() > matchedSite.getName().length())) {

					matchedSite = site;
				}
			}
		}

		return matchedSite;
	}

	private static List<SiteData> _getMatchedSites(
		String queryType, SiteData siteFilter, List<SiteData> sites) {

		List<SiteData> candidateSites;

		if (siteFilter != null) {
			candidateSites = List.of(siteFilter);
		}
		else {
			candidateSites = new ArrayList<>(sites);
		}

		List<SiteData> matchedSites = new ArrayList<>();

		for (SiteData site : candidateSites) {
			SiteData matchedSite = _toMatchedSite(queryType, site);

			if (matchedSite != null) {
				matchedSites.add(matchedSite);
			}
		}

		_sort(queryType, matchedSites);

		return List.copyOf(matchedSites);
	}

	private static String _getQueryType(String prompt) {
		String normalizedPrompt = prompt.toLowerCase(Locale.ROOT);

		if (normalizedPrompt.contains("membership") ||
			normalizedPrompt.contains("members")) {

			return SiteQueryData.TYPE_SITE_MEMBERSHIP;
		}

		if (normalizedPrompt.contains("activity")) {
			return SiteQueryData.TYPE_SITE_ACTIVITY;
		}

		if (normalizedPrompt.contains("private page")) {
			return SiteQueryData.TYPE_PRIVATE_PAGES;
		}

		if (normalizedPrompt.contains("public page")) {
			return SiteQueryData.TYPE_PUBLIC_PAGES;
		}

		if (normalizedPrompt.contains("page")) {
			return SiteQueryData.TYPE_SITE_PAGES;
		}

		return SiteQueryData.TYPE_SITES;
	}

	private static boolean _matches(String normalizedPrompt, SiteData site) {
		String normalizedFriendlyURL = site.getFriendlyURL(
		).toLowerCase(
			Locale.ROOT);
		String normalizedName = site.getName(
		).toLowerCase(
			Locale.ROOT);

		return normalizedPrompt.contains(normalizedName) ||
			normalizedPrompt.contains(normalizedFriendlyURL) ||
			normalizedPrompt.contains(
				normalizedFriendlyURL.startsWith("/") ?
					normalizedFriendlyURL.substring(1) : normalizedFriendlyURL);
	}

	private static void _sort(String queryType, List<SiteData> sites) {
		if (SiteQueryData.TYPE_SITE_ACTIVITY.equals(queryType)) {
			sites.sort(
				Comparator.comparing(
					SiteData::getLastModifiedDate,
					Comparator.nullsLast(String::compareTo))
				.reversed());

			return;
		}

		if (SiteQueryData.TYPE_SITE_MEMBERSHIP.equals(queryType)) {
			sites.sort(Comparator.comparingInt(SiteData::getUserCount).reversed());

			return;
		}

		sites.sort(
			Comparator.comparing(
				SiteData::getName, String.CASE_INSENSITIVE_ORDER));
	}

	private static SiteData _stripPages(SiteData site) {
		return new SiteData(
			site.isActive(), site.getCompanyId(), site.getFriendlyURL(),
			site.getGroupId(), site.getLastModifiedDate(), site.getName(),
			site.getPrivatePageCount(), List.of(), site.getPublicPageCount(),
			List.of(), site.isSite(), site.getUserCount());
	}

	private static SiteData _toMatchedSite(String queryType, SiteData site) {
		if (SiteQueryData.TYPE_PUBLIC_PAGES.equals(queryType)) {
			if (site.getPublicPages().isEmpty()) {
				return null;
			}

			return new SiteData(
				site.isActive(), site.getCompanyId(), site.getFriendlyURL(),
				site.getGroupId(), site.getLastModifiedDate(), site.getName(),
			0, List.of(), site.getPublicPageCount(), site.getPublicPages(),
				site.isSite(), site.getUserCount());
		}

		if (SiteQueryData.TYPE_PRIVATE_PAGES.equals(queryType)) {
			if (site.getPrivatePages().isEmpty()) {
				return null;
			}

			return new SiteData(
				site.isActive(), site.getCompanyId(), site.getFriendlyURL(),
				site.getGroupId(), site.getLastModifiedDate(), site.getName(),
				site.getPrivatePageCount(), site.getPrivatePages(), 0, List.of(),
				site.isSite(), site.getUserCount());
		}

		if (SiteQueryData.TYPE_SITE_PAGES.equals(queryType)) {
			return site;
		}

		return _stripPages(site);
	}

	private SiteQueryDataFactory() {
	}

}
