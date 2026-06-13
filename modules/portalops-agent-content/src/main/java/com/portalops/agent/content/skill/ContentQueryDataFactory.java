package com.portalops.agent.content.skill;

import com.portalops.agent.content.dto.ContentData;
import com.portalops.agent.content.dto.ContentQueryData;
import com.portalops.api.content.ContentSummary;

import java.util.List;

public class ContentQueryDataFactory {

	public static ContentQueryData createAll(ContentData contentData) {
		return _create(
			contentData, ContentQueryData.TYPE_CONTENT_SUMMARY,
			contentData.getContent());
	}

	public static ContentQueryData createExpired(ContentData contentData) {
		return _create(
			contentData, ContentQueryData.TYPE_EXPIRED_CONTENT,
			contentData.getExpiredContent());
	}

	public static ContentQueryData createPending(ContentData contentData) {
		return _create(
			contentData, ContentQueryData.TYPE_PENDING_CONTENT,
			contentData.getPendingContent());
	}

	private static ContentQueryData _create(
		ContentData contentData, String queryType,
		List<ContentSummary> matchedContent) {

		return new ContentQueryData(
			contentData.getCompanyId(), matchedContent, "content",
			contentData.getExpiredContent().size(), matchedContent.size(),
			contentData.getPendingContent().size(), queryType, "currentCompany",
			contentData.getContent().size());
	}

	private ContentQueryDataFactory() {
	}

}
