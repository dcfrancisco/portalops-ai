package com.portalops.agent.site.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SiteData implements Serializable {

	public SiteData(
		boolean active, long companyId, String friendlyURL, long groupId,
		String lastModifiedDate, String name, int privatePageCount,
		List<PageData> privatePages, int publicPageCount,
		List<PageData> publicPages, boolean site, int userCount) {

		_active = active;
		_companyId = companyId;
		_friendlyURL = Objects.requireNonNull(friendlyURL);
		_groupId = groupId;
		_lastModifiedDate = lastModifiedDate;
		_name = Objects.requireNonNull(name);
		_privatePageCount = privatePageCount;
		_privatePages = List.copyOf(Objects.requireNonNull(privatePages));
		_publicPageCount = publicPageCount;
		_publicPages = List.copyOf(Objects.requireNonNull(publicPages));
		_site = site;
		_userCount = userCount;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getFriendlyURL() {
		return _friendlyURL;
	}

	public long getGroupId() {
		return _groupId;
	}

	public String getLastModifiedDate() {
		return _lastModifiedDate;
	}

	public String getName() {
		return _name;
	}

	public int getPrivatePageCount() {
		return _privatePageCount;
	}

	public List<PageData> getPrivatePages() {
		return _privatePages;
	}

	public int getPublicPageCount() {
		return _publicPageCount;
	}

	public List<PageData> getPublicPages() {
		return _publicPages;
	}

	public int getUserCount() {
		return _userCount;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean isSite() {
		return _site;
	}

	private final boolean _active;
	private final long _companyId;
	private final String _friendlyURL;
	private final long _groupId;
	private final String _lastModifiedDate;
	private final String _name;
	private final int _privatePageCount;
	private final List<PageData> _privatePages;
	private final int _publicPageCount;
	private final List<PageData> _publicPages;
	private final boolean _site;
	private final int _userCount;

}
