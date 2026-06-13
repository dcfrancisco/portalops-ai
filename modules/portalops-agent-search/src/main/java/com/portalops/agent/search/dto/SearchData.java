package com.portalops.agent.search.dto;

import com.portalops.api.search.SearchSummary;

import java.io.Serializable;
import java.util.Objects;

public class SearchData implements Serializable {

	public SearchData(SearchSummary searchSummary) {
		_searchSummary = Objects.requireNonNull(searchSummary);
	}

	public SearchSummary getSearchSummary() {
		return _searchSummary;
	}

	private final SearchSummary _searchSummary;

}
