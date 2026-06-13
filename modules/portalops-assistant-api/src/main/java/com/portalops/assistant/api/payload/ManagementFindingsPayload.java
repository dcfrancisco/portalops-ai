package com.portalops.assistant.api.payload;

public class ManagementFindingsPayload implements AssistantPayload {

	public ManagementFindingsPayload(
		String queryType, String subject, int totalAgents,
		int totalCapabilities, int totalDomains, int totalSkills) {

		_queryType = queryType;
		_subject = subject;
		_totalAgents = totalAgents;
		_totalCapabilities = totalCapabilities;
		_totalDomains = totalDomains;
		_totalSkills = totalSkills;
	}

	public String getQueryType() {
		return _queryType;
	}

	public String getSubject() {
		return _subject;
	}

	public int getTotalAgents() {
		return _totalAgents;
	}

	public int getTotalCapabilities() {
		return _totalCapabilities;
	}

	public int getTotalDomains() {
		return _totalDomains;
	}

	public int getTotalSkills() {
		return _totalSkills;
	}

	private final String _queryType;
	private final String _subject;
	private final int _totalAgents;
	private final int _totalCapabilities;
	private final int _totalDomains;
	private final int _totalSkills;

}
