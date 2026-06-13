package com.portalops.agent.search.skill;

import com.portalops.agent.search.dto.SearchData;
import com.portalops.agent.search.dto.SearchQueryData;
import com.portalops.api.search.SearchDiagnostic;
import com.portalops.api.search.SearchSummary;

public class SearchQueryDataFactory {

	public static SearchQueryData createErrors(SearchData searchData) {
		return _create(
			SearchQueryData.TYPE_SEARCH_ERRORS,
			searchData.getSearchSummary());
	}

	public static SearchQueryData createHealth(SearchData searchData) {
		return _create(
			SearchQueryData.TYPE_SEARCH_HEALTH,
			searchData.getSearchSummary());
	}

	public static SearchQueryData createReindex(SearchData searchData) {
		return _create(
			SearchQueryData.TYPE_REINDEX_STATUS,
			searchData.getSearchSummary());
	}

	private static SearchQueryData _create(
		String queryType, SearchSummary searchSummary) {

		return new SearchQueryData(
			searchSummary.getCompanyId(), searchSummary.getCompanyIndexName(),
			searchSummary.getDiagnostics(),
			(int)searchSummary.getDiagnostics(
			).stream(
			).filter(
				searchDiagnostic -> {
					String severity = searchDiagnostic.getSeverity();

					return "critical".equals(severity) ||
						"warning".equals(severity);
				}
			).count(),
			searchSummary.getHealthState(), searchSummary.isIndexExists(),
			searchSummary.isIndexReadOnly(),
			searchSummary.getIndexSizeInBytes(),
			searchSummary.getIndexedDocuments(),
			searchSummary.getLastReindexDate(),
			searchSummary.getLastReindexStatus(), queryType,
			searchSummary.getReindexTaskCount(),
			searchSummary.isReindexRequired(),
			searchSummary.isSearchEnabled(),
			searchSummary.getSearchEngine(), searchSummary.getWarnings());
	}

	private SearchQueryDataFactory() {
	}

}
