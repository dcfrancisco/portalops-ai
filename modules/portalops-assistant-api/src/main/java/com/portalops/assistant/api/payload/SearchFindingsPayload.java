package com.portalops.assistant.api.payload;

public class SearchFindingsPayload implements AssistantPayload {

	public SearchFindingsPayload(
		int diagnosticsCount, String healthState, long indexedDocuments,
		boolean indexExists, boolean indexReadOnly, String lastReindexDate,
		String queryType, int reindexTaskCount, boolean reindexRequired,
		boolean searchEnabled, String searchEngine, int warningsCount) {

		_diagnosticsCount = diagnosticsCount;
		_healthState = healthState;
		_indexedDocuments = indexedDocuments;
		_indexExists = indexExists;
		_indexReadOnly = indexReadOnly;
		_lastReindexDate = lastReindexDate;
		_queryType = queryType;
		_reindexTaskCount = reindexTaskCount;
		_reindexRequired = reindexRequired;
		_searchEnabled = searchEnabled;
		_searchEngine = searchEngine;
		_warningsCount = warningsCount;
	}

	public int getDiagnosticsCount() {
		return _diagnosticsCount;
	}

	public String getHealthState() {
		return _healthState;
	}

	public long getIndexedDocuments() {
		return _indexedDocuments;
	}

	public String getLastReindexDate() {
		return _lastReindexDate;
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

	public int getWarningsCount() {
		return _warningsCount;
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

	private final int _diagnosticsCount;
	private final String _healthState;
	private final long _indexedDocuments;
	private final boolean _indexExists;
	private final boolean _indexReadOnly;
	private final String _lastReindexDate;
	private final String _queryType;
	private final int _reindexTaskCount;
	private final boolean _reindexRequired;
	private final boolean _searchEnabled;
	private final String _searchEngine;
	private final int _warningsCount;

}
