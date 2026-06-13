package com.portalops.agent.search.dto;

import com.portalops.api.search.SearchDiagnostic;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SearchQueryData implements Serializable {

	public static final String TYPE_REINDEX_STATUS = "reindex-status";
	public static final String TYPE_SEARCH_ERRORS = "search-errors";
	public static final String TYPE_SEARCH_HEALTH = "search-health";

	public SearchQueryData(
		long companyId, String companyIndexName,
		List<SearchDiagnostic> diagnostics, int errorCount,
		String healthState, boolean indexExists, boolean indexReadOnly,
		long indexSizeInBytes, long indexedDocuments, String lastReindexDate,
		String lastReindexStatus, String queryType, int reindexTaskCount,
		boolean reindexRequired, boolean searchEnabled,
		String searchEngine, List<String> warnings) {

		_companyId = companyId;
		_companyIndexName = Objects.requireNonNull(companyIndexName);
		_diagnostics = List.copyOf(Objects.requireNonNull(diagnostics));
		_errorCount = errorCount;
		_healthState = Objects.requireNonNull(healthState);
		_indexExists = indexExists;
		_indexReadOnly = indexReadOnly;
		_indexSizeInBytes = indexSizeInBytes;
		_indexedDocuments = indexedDocuments;
		_lastReindexDate = lastReindexDate;
		_lastReindexStatus = lastReindexStatus;
		_queryType = Objects.requireNonNull(queryType);
		_reindexTaskCount = reindexTaskCount;
		_reindexRequired = reindexRequired;
		_searchEnabled = searchEnabled;
		_searchEngine = Objects.requireNonNull(searchEngine);
		_warnings = List.copyOf(Objects.requireNonNull(warnings));
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getCompanyIndexName() {
		return _companyIndexName;
	}

	public List<SearchDiagnostic> getDiagnostics() {
		return _diagnostics;
	}

	public int getErrorCount() {
		return _errorCount;
	}

	public String getHealthState() {
		return _healthState;
	}

	public long getIndexedDocuments() {
		return _indexedDocuments;
	}

	public long getIndexSizeInBytes() {
		return _indexSizeInBytes;
	}

	public String getLastReindexDate() {
		return _lastReindexDate;
	}

	public String getLastReindexStatus() {
		return _lastReindexStatus;
	}

	public String getQueryType() {
		return _queryType;
	}

	public int getReindexTaskCount() {
		return _reindexTaskCount;
	}

	public String getSearchEngine() {
		return _searchEngine;
	}

	public List<String> getWarnings() {
		return _warnings;
	}

	public boolean isIndexExists() {
		return _indexExists;
	}

	public boolean isIndexReadOnly() {
		return _indexReadOnly;
	}

	public boolean isReindexRequired() {
		return _reindexRequired;
	}

	public boolean isSearchEnabled() {
		return _searchEnabled;
	}

	private final long _companyId;
	private final String _companyIndexName;
	private final List<SearchDiagnostic> _diagnostics;
	private final int _errorCount;
	private final String _healthState;
	private final boolean _indexExists;
	private final boolean _indexReadOnly;
	private final long _indexSizeInBytes;
	private final long _indexedDocuments;
	private final String _lastReindexDate;
	private final String _lastReindexStatus;
	private final String _queryType;
	private final int _reindexTaskCount;
	private final boolean _reindexRequired;
	private final boolean _searchEnabled;
	private final String _searchEngine;
	private final List<String> _warnings;

}
