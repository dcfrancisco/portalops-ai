package com.portalops.api.search;

import com.portalops.api.service.PortalOpsRequestContext;

public interface SearchInspectionService {

	public SearchSummary getSearchErrors(PortalOpsRequestContext context);

	public SearchSummary getSearchHealth(PortalOpsRequestContext context);

	public SearchSummary getReindexStatus(PortalOpsRequestContext context);

}
