package com.portalops.content.internal;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.portalops.api.content.ContentInspectionService;
import com.portalops.api.content.ContentSummary;
import com.portalops.api.service.PortalOpsRequestContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ContentInspectionService.class)
public class PortalOpsContentInspectionServiceComponent
        implements ContentInspectionService {

    @Override
    public List<ContentSummary> getContentSummary(PortalOpsRequestContext context) {
        return _toContentSummaries(
                _getLatestCompanyArticles(context.getCompanyId()));
    }

    @Override
    public List<ContentSummary> getExpiredContent(PortalOpsRequestContext context) {
        return _toContentSummaries(
                _getLatestCompanyArticles(
                        context.getCompanyId()
                ).stream(
                ).filter(
                        JournalArticle::isExpired
                ).toList());
    }

    @Override
    public List<ContentSummary> getPendingContent(PortalOpsRequestContext context) {
        return _toContentSummaries(
                _getLatestCompanyArticles(
                        context.getCompanyId()
                ).stream(
                ).filter(
                        article -> article.isPending() || article.isDraft()
                ).toList());
    }

    @Override
    public List<ContentSummary> getStaleContent(PortalOpsRequestContext context) {
        return getExpiredContent(context);
    }

    @Override
    public List<ContentSummary> getUnpublishedDrafts(
            PortalOpsRequestContext context) {

        return getPendingContent(context);
    }

    private List<JournalArticle> _getLatestCompanyArticles(long companyId) {
        List<JournalArticle> articles =
                _journalArticleLocalService.getCompanyArticles(
                        companyId, WorkflowConstants.STATUS_ANY,
                        QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        Map<Long, JournalArticle> latestArticles = new LinkedHashMap<>();

        for (JournalArticle article : articles) {
            JournalArticle existingArticle = latestArticles.get(
                    article.getResourcePrimKey());

            if ((existingArticle == null) ||
                (article.getVersion() > existingArticle.getVersion())) {

                latestArticles.put(article.getResourcePrimKey(), article);
            }
        }

        List<JournalArticle> latestArticleList = new ArrayList<>(
                latestArticles.values());

        latestArticleList.sort(
                Comparator.comparing(
                        JournalArticle::getModifiedDate,
                        Comparator.nullsLast(Date::compareTo))
                .reversed());

        return latestArticleList;
    }

    private String _getContentType(JournalArticle article) {
        String ddmStructureKey = article.getDDMStructureKey();

        if ((ddmStructureKey == null) || ddmStructureKey.isBlank()) {
            return "Web Content";
        }

        return ddmStructureKey;
    }

    private List<ContentSummary> _toContentSummaries(
            List<JournalArticle> articles) {

        List<ContentSummary> contentSummaries = new ArrayList<>();

        for (JournalArticle article : articles) {
            String status = WorkflowConstants.getStatusLabel(article.getStatus());
            String type = _getContentType(article);

            contentSummaries.add(
                    new ContentSummary(
                            article.getResourcePrimKey(),
                            article.getTitleCurrentValue(),
                            status, type));
        }

        return List.copyOf(contentSummaries);
    }

    @Reference
    private JournalArticleLocalService _journalArticleLocalService;

}
