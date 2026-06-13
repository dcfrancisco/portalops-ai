package com.portalops.agent.content.dto;

import com.portalops.api.content.ContentSummary;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ContentQueryData implements Serializable {

	public static final String TYPE_CONTENT_SUMMARY = "content-summary";
	public static final String TYPE_EXPIRED_CONTENT = "expired-content";
	public static final String TYPE_PENDING_CONTENT = "pending-content";

	public ContentQueryData(
		long companyId, List<ContentSummary> content, String domain,
		int expiredContent, int matchedContent, int pendingContent,
		String queryType, String scope, int totalContent) {

		_companyId = companyId;
		_content = List.copyOf(Objects.requireNonNull(content));
		_domain = Objects.requireNonNull(domain);
		_expiredContent = expiredContent;
		_matchedContent = matchedContent;
		_pendingContent = pendingContent;
		_queryType = Objects.requireNonNull(queryType);
		_scope = Objects.requireNonNull(scope);
		_totalContent = totalContent;
	}

	public List<ContentSummary> getContent() {
		return _content;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getDomain() {
		return _domain;
	}

	public int getExpiredContent() {
		return _expiredContent;
	}

	public int getMatchedContent() {
		return _matchedContent;
	}

	public int getPendingContent() {
		return _pendingContent;
	}

	public String getQueryType() {
		return _queryType;
	}

	public String getScope() {
		return _scope;
	}

	public int getTotalContent() {
		return _totalContent;
	}

	private final List<ContentSummary> _content;
	private final long _companyId;
	private final String _domain;
	private final int _expiredContent;
	private final int _matchedContent;
	private final int _pendingContent;
	private final String _queryType;
	private final String _scope;
	private final int _totalContent;

}
