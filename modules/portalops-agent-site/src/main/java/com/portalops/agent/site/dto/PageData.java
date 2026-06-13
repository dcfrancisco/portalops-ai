package com.portalops.agent.site.dto;

import java.io.Serializable;
import java.util.Objects;

public class PageData implements Serializable {

	public PageData(
		String friendlyURL, boolean hidden, long layoutId, String name,
		Long parentLayoutId, boolean privateLayout, long plid, String type) {

		_friendlyURL = Objects.requireNonNull(friendlyURL);
		_hidden = hidden;
		_layoutId = layoutId;
		_name = Objects.requireNonNull(name);
		_parentLayoutId = parentLayoutId;
		_privateLayout = privateLayout;
		_plid = plid;
		_type = Objects.requireNonNull(type);
	}

	public String getFriendlyURL() {
		return _friendlyURL;
	}

	public long getLayoutId() {
		return _layoutId;
	}

	public String getName() {
		return _name;
	}

	public Long getParentLayoutId() {
		return _parentLayoutId;
	}

	public long getPlid() {
		return _plid;
	}

	public String getType() {
		return _type;
	}

	public boolean isHidden() {
		return _hidden;
	}

	public boolean isPrivateLayout() {
		return _privateLayout;
	}

	private final String _friendlyURL;
	private final boolean _hidden;
	private final long _layoutId;
	private final String _name;
	private final Long _parentLayoutId;
	private final boolean _privateLayout;
	private final long _plid;
	private final String _type;

}
