package com.portalops.search.internal;

import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.engine.SearchEngineInformation;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.cluster.ClusterHealthStatus;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;
import com.liferay.portal.search.engine.adapter.index.StatsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.StatsIndexResponse;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.CountSearchResponse;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.index.IndexStatusManager;

import com.portalops.api.search.SearchDiagnostic;
import com.portalops.api.search.SearchInspectionService;
import com.portalops.api.search.SearchSummary;
import com.portalops.api.service.PortalOpsRequestContext;

import java.time.Instant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = SearchInspectionService.class)
public class PortalOpsSearchInspectionServiceComponent
	implements SearchInspectionService {

	@Override
	public SearchSummary getSearchErrors(PortalOpsRequestContext context) {
		return _getSearchSummary(context);
	}

	@Override
	public SearchSummary getSearchHealth(PortalOpsRequestContext context) {
		return _getSearchSummary(context);
	}

	@Override
	public SearchSummary getReindexStatus(PortalOpsRequestContext context) {
		return _getSearchSummary(context);
	}

	private String _getClusterHealthState(String indexName) {
		try {
			HealthClusterResponse healthClusterResponse =
				_searchEngineAdapter.execute(
					new HealthClusterRequest(indexName));

			ClusterHealthStatus clusterHealthStatus =
				healthClusterResponse.getClusterHealthStatus();

			if (clusterHealthStatus == null) {
				return "unknown";
			}

			return StringUtil.toLowerCase(clusterHealthStatus.name());
		}
		catch (RuntimeException runtimeException) {
			return "unknown";
		}
	}

	private List<SearchDiagnostic> _getDiagnostics(
		boolean searchEnabled, boolean indexExists, boolean indexReadOnly,
		String healthState, int reindexTaskCount, String lastReindexStatus) {

		List<SearchDiagnostic> diagnostics = new ArrayList<>();

		if (!searchEnabled) {
			diagnostics.add(
				new SearchDiagnostic(
					"Search does not appear to be available for this runtime.",
					"critical", "Search unavailable"));

			return List.copyOf(diagnostics);
		}

		if (!indexExists) {
			diagnostics.add(
				new SearchDiagnostic(
					"The company search index does not currently exist.",
					"critical", "Index missing"));
		}

		if (indexReadOnly) {
			diagnostics.add(
				new SearchDiagnostic(
					"The company search index is read-only.",
					"warning", "Index read-only"));
		}

		if ("red".equals(healthState)) {
			diagnostics.add(
				new SearchDiagnostic(
					"Cluster health is red and requires immediate review.",
					"critical", "Cluster health red"));
		}
		else if ("yellow".equals(healthState)) {
			diagnostics.add(
				new SearchDiagnostic(
					"Cluster health is yellow and should be reviewed.",
					"warning", "Cluster health yellow"));
		}

		if (reindexTaskCount > 0) {
			diagnostics.add(
				new SearchDiagnostic(
					"Reindex work is currently in progress for this company.",
					"info", "Reindex in progress"));
		}

		if ((lastReindexStatus != null) &&
			lastReindexStatus.toLowerCase(
				Locale.ROOT
			).contains(
				"failed")) {

			diagnostics.add(
				new SearchDiagnostic(
					"The latest detected reindex background task did not complete cleanly.",
					"warning", "Latest reindex needs review"));
		}

		return List.copyOf(diagnostics);
	}

	private long _getIndexedDocuments(String indexName) {
		try {
			CountSearchRequest countSearchRequest = new CountSearchRequest();

			countSearchRequest.setIndexNames(indexName);

			CountSearchResponse countSearchResponse =
				_searchEngineAdapter.execute(countSearchRequest);

			return countSearchResponse.getCount();
		}
		catch (RuntimeException runtimeException) {
			return 0;
		}
	}

	private long _getIndexSizeInBytes(String indexName) {
		try {
			StatsIndexResponse statsIndexResponse =
				_searchEngineAdapter.execute(new StatsIndexRequest(indexName));

			return statsIndexResponse.getIndexSizeInBytes(indexName);
		}
		catch (RuntimeException runtimeException) {
			return 0;
		}
	}

	private BackgroundTask _getLatestReindexBackgroundTask(long companyId) {
		List<BackgroundTask> backgroundTasks =
			_backgroundTaskLocalService.getBackgroundTasks(0, 200);
		List<BackgroundTask> reindexBackgroundTasks = new ArrayList<>();

		for (BackgroundTask backgroundTask : backgroundTasks) {
			if ((backgroundTask.getCompanyId() == companyId) &&
				_isReindexBackgroundTask(backgroundTask)) {

				reindexBackgroundTasks.add(backgroundTask);
			}
		}

		reindexBackgroundTasks.sort(
			Comparator.comparing(
				BackgroundTask::getCreateDate,
				Comparator.nullsLast(Date::compareTo))
			.reversed());

		if (reindexBackgroundTasks.isEmpty()) {
			return null;
		}

		return reindexBackgroundTasks.get(0);
	}

	private SearchSummary _getSearchSummary(PortalOpsRequestContext context) {
		long companyId = context.getCompanyId();
		String companyIndexName = _indexNameBuilder.getIndexName(companyId);
		boolean searchEnabled = _isSearchEnabled();
		String searchEngine = _getSearchEngine();
		boolean indexExists = false;
		boolean indexReadOnly = false;
		long indexedDocuments = 0;
		long indexSizeInBytes = 0;
		int reindexTaskCount = 0;
		String lastReindexDate = null;
		String lastReindexStatus = null;
		String healthState = "unavailable";

		if (searchEnabled) {
			indexExists = _isIndexExists(companyIndexName);
			indexReadOnly = _indexStatusManager.isIndexReadOnly(companyIndexName);
			reindexTaskCount = _getReindexTaskCount(companyId);

			BackgroundTask backgroundTask = _getLatestReindexBackgroundTask(
				companyId);

			if (backgroundTask != null) {
				lastReindexDate = _toInstant(
					(backgroundTask.getCompletionDate() != null) ?
						backgroundTask.getCompletionDate() :
						backgroundTask.getCreateDate());
				lastReindexStatus = backgroundTask.getStatusLabel();
			}

			if (indexExists) {
				indexedDocuments = _getIndexedDocuments(companyIndexName);
				indexSizeInBytes = _getIndexSizeInBytes(companyIndexName);
				healthState = _getClusterHealthState(companyIndexName);
			}
			else {
				healthState = "warning";
			}
		}

		List<SearchDiagnostic> diagnostics = _getDiagnostics(
			searchEnabled, indexExists, indexReadOnly, healthState,
			reindexTaskCount, lastReindexStatus);
		List<String> warnings = diagnostics.stream(
		).filter(
			searchDiagnostic -> !"info".equals(searchDiagnostic.getSeverity())
		).map(
			SearchDiagnostic::getTitle
		).toList();
		boolean reindexRequired =
			!searchEnabled || !indexExists || "red".equals(healthState) ||
				((lastReindexStatus != null) &&
				 lastReindexStatus.toLowerCase(
					 Locale.ROOT
				 ).contains(
					 "failed"));

		return new SearchSummary(
			companyId, companyIndexName, diagnostics, healthState, indexExists,
			indexReadOnly, indexSizeInBytes, indexedDocuments, lastReindexDate,
			lastReindexStatus, reindexTaskCount, reindexRequired, searchEnabled,
			searchEngine, warnings);
	}

	private int _getReindexTaskCount(long companyId) {
		try {
			return _indexWriterHelper.getReindexTaskCount(companyId, false);
		}
		catch (SearchException searchException) {
			return 0;
		}
		catch (RuntimeException runtimeException) {
			return 0;
		}
	}

	private String _getSearchEngine() {
		try {
			String vendorString = _searchEngineInformation.getVendorString();

			if ((vendorString != null) && !vendorString.isBlank()) {
				return vendorString;
			}
		}
		catch (RuntimeException runtimeException) {
		}

		SearchEngine searchEngine = _searchEngineHelper.getSearchEngine();

		if (searchEngine == null) {
			return "Unavailable";
		}

		return searchEngine.getVendor();
	}

	private boolean _isIndexExists(String companyIndexName) {
		try {
			IndicesExistsIndexResponse indicesExistsIndexResponse =
				_searchEngineAdapter.execute(
					new IndicesExistsIndexRequest(companyIndexName));

			return indicesExistsIndexResponse.isExists();
		}
		catch (RuntimeException runtimeException) {
			return false;
		}
	}

	private boolean _isReindexBackgroundTask(BackgroundTask backgroundTask) {
		String name = StringUtil.toLowerCase(backgroundTask.getName());
		String taskExecutorClassName = StringUtil.toLowerCase(
			backgroundTask.getTaskExecutorClassName());
		Map<String, java.io.Serializable> taskContextMap =
			backgroundTask.getTaskContextMap();

		return name.contains("reindex") ||
			taskExecutorClassName.contains("reindex") ||
			taskContextMap.containsKey("portalStart") ||
			taskContextMap.containsKey("singleStart");
	}

	private boolean _isSearchEnabled() {
		return _searchEngineHelper.getSearchEngine() != null;
	}

	private String _toInstant(Date date) {
		if (date == null) {
			return null;
		}

		return Instant.ofEpochMilli(date.getTime()).toString();
	}

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private IndexNameBuilder _indexNameBuilder;

	@Reference
	private IndexStatusManager _indexStatusManager;

	@Reference
	private IndexWriterHelper _indexWriterHelper;

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

	@Reference
	private SearchEngineHelper _searchEngineHelper;

	@Reference
	private SearchEngineInformation _searchEngineInformation;

}
