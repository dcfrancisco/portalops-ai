package com.portalops.agent.site.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SitesData implements Serializable {

	public SitesData(long companyId, List<SiteData> sites) {
		_companyId = companyId;
		_sites = List.copyOf(Objects.requireNonNull(sites));
	}

	public long getCompanyId() {
		return _companyId;
	}

	public List<SiteData> getSites() {
		return _sites;
	}

	private final long _companyId;
	private final List<SiteData> _sites;

}
