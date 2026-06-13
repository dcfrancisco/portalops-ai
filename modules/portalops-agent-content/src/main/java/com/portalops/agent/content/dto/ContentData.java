package com.portalops.agent.content.dto;

import com.portalops.api.content.ContentSummary;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ContentData implements Serializable {

	public ContentData(
		long companyId, List<ContentSummary> content,
		List<ContentSummary> expiredContent,
		List<ContentSummary> pendingContent) {

		_companyId = companyId;
		_content = List.copyOf(Objects.requireNonNull(content));
		_expiredContent = List.copyOf(Objects.requireNonNull(expiredContent));
		_pendingContent = List.copyOf(Objects.requireNonNull(pendingContent));
	}

	public List<ContentSummary> getContent() {
		return _content;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public List<ContentSummary> getExpiredContent() {
		return _expiredContent;
	}

	public List<ContentSummary> getPendingContent() {
		return _pendingContent;
	}

	private final List<ContentSummary> _content;
	private final long _companyId;
	private final List<ContentSummary> _expiredContent;
	private final List<ContentSummary> _pendingContent;

}
