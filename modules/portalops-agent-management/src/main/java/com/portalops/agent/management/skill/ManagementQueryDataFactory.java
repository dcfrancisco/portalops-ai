package com.portalops.agent.management.skill;

import com.liferay.petra.string.StringPool;

import com.portalops.agent.management.dto.AgentMetadataData;
import com.portalops.agent.management.dto.CapabilityMetadataData;
import com.portalops.agent.management.dto.DomainMetadataData;
import com.portalops.agent.management.dto.ManagementQueryData;
import com.portalops.agent.management.dto.RuntimeMetadataData;
import com.portalops.agent.management.dto.SkillMetadataData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManagementQueryDataFactory {

	public static ManagementQueryData createDescribe(
		String prompt, RuntimeMetadataData runtimeMetadataData) {

		String subject = _getSubject(prompt);
		String normalizedSubject = subject.toLowerCase(Locale.ROOT);

		return new ManagementQueryData(
			_filterAgents(normalizedSubject, runtimeMetadataData.getAgents()),
			_filterCapabilities(
				normalizedSubject, runtimeMetadataData.getCapabilities()),
			_filterDomains(normalizedSubject, runtimeMetadataData.getDomains()),
			ManagementQueryData.TYPE_DESCRIBE_CAPABILITY,
			_filterSkills(normalizedSubject, runtimeMetadataData.getSkills()),
			subject, runtimeMetadataData.getAgents().size(),
			runtimeMetadataData.getCapabilities().size(),
			runtimeMetadataData.getDomains().size(),
			runtimeMetadataData.getSkills().size());
	}

	public static ManagementQueryData createListAgents(
		RuntimeMetadataData runtimeMetadataData) {

		return _create(
			ManagementQueryData.TYPE_LIST_AGENTS, runtimeMetadataData,
			StringPool.BLANK);
	}

	public static ManagementQueryData createListCapabilities(
		RuntimeMetadataData runtimeMetadataData) {

		return _create(
			ManagementQueryData.TYPE_LIST_CAPABILITIES, runtimeMetadataData,
			StringPool.BLANK);
	}

	public static ManagementQueryData createListDomains(
		RuntimeMetadataData runtimeMetadataData) {

		return _create(
			ManagementQueryData.TYPE_LIST_DOMAINS, runtimeMetadataData,
			StringPool.BLANK);
	}

	public static ManagementQueryData createListSkills(
		RuntimeMetadataData runtimeMetadataData) {

		return _create(
			ManagementQueryData.TYPE_LIST_SKILLS, runtimeMetadataData,
			StringPool.BLANK);
	}

	private static ManagementQueryData _create(
		String queryType, RuntimeMetadataData runtimeMetadataData,
		String subject) {

		return new ManagementQueryData(
			runtimeMetadataData.getAgents(), runtimeMetadataData.getCapabilities(),
			runtimeMetadataData.getDomains(), queryType,
			runtimeMetadataData.getSkills(), subject,
			runtimeMetadataData.getAgents().size(),
			runtimeMetadataData.getCapabilities().size(),
			runtimeMetadataData.getDomains().size(),
			runtimeMetadataData.getSkills().size());
	}

	private static List<AgentMetadataData> _filterAgents(
		String normalizedSubject, List<AgentMetadataData> agents) {

		List<AgentMetadataData> matchedAgents = new ArrayList<>();

		for (AgentMetadataData agentMetadataData : agents) {
			if (_matches(normalizedSubject, agentMetadataData.getDomain()) ||
				_matches(normalizedSubject, agentMetadataData.getName()) ||
				_matches(normalizedSubject, agentMetadataData.getDescription())) {

				matchedAgents.add(agentMetadataData);
			}
		}

		return List.copyOf(matchedAgents);
	}

	private static List<CapabilityMetadataData> _filterCapabilities(
		String normalizedSubject,
		List<CapabilityMetadataData> capabilities) {

		List<CapabilityMetadataData> matchedCapabilities = new ArrayList<>();

		for (CapabilityMetadataData capabilityMetadataData : capabilities) {
			if (_matches(normalizedSubject, capabilityMetadataData.getDomain()) ||
				_matches(normalizedSubject, capabilityMetadataData.getName()) ||
				_matches(
					normalizedSubject,
					capabilityMetadataData.getOwnerName())) {

				matchedCapabilities.add(capabilityMetadataData);
			}
		}

		return List.copyOf(matchedCapabilities);
	}

	private static List<DomainMetadataData> _filterDomains(
		String normalizedSubject, List<DomainMetadataData> domains) {

		List<DomainMetadataData> matchedDomains = new ArrayList<>();

		for (DomainMetadataData domainMetadataData : domains) {
			if (_matches(normalizedSubject, domainMetadataData.getName()) ||
				_matches(
					normalizedSubject, domainMetadataData.getDescription())) {

				matchedDomains.add(domainMetadataData);
			}
		}

		return List.copyOf(matchedDomains);
	}

	private static List<SkillMetadataData> _filterSkills(
		String normalizedSubject, List<SkillMetadataData> skills) {

		List<SkillMetadataData> matchedSkills = new ArrayList<>();

		for (SkillMetadataData skillMetadataData : skills) {
			if (_matches(normalizedSubject, skillMetadataData.getDomain()) ||
				_matches(normalizedSubject, skillMetadataData.getName()) ||
				_matches(normalizedSubject, skillMetadataData.getDescription())) {

				matchedSkills.add(skillMetadataData);
			}
		}

		return List.copyOf(matchedSkills);
	}

	private static String _getSubject(String prompt) {
		String normalizedPrompt = prompt.trim();
		int index = normalizedPrompt.toLowerCase(Locale.ROOT).indexOf("describe");

		if (index < 0) {
			return normalizedPrompt;
		}

		return normalizedPrompt.substring(index + "describe".length()).trim();
	}

	private static boolean _matches(String normalizedSubject, String candidate) {
		return candidate.toLowerCase(Locale.ROOT).contains(normalizedSubject);
	}

	private ManagementQueryDataFactory() {
	}

}
