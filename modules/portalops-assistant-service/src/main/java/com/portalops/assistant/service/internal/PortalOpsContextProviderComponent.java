package com.portalops.assistant.service.internal;

import com.liferay.petra.string.StringBundler;

import com.portalops.api.runtime.PortalOpsAgent;
import com.portalops.api.runtime.PortalOpsSkill;
import com.portalops.api.runtime.PortalOpsTool;
import com.portalops.assistant.api.PortalOpsContextProvider;
import com.portalops.assistant.api.PortalOpsExecutionMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = PortalOpsContextProvider.class)
public class PortalOpsContextProviderComponent implements PortalOpsContextProvider {

	@Override
	public String buildRuntimeContext() {
		StringBundler sb = new StringBundler();

		sb.append("PortalOps runtime metadata is available.\n\n");
		sb.append("Registered PortalOps Agents:\n");
		_appendAgents(sb);
		sb.append("\nRegistered PortalOps Skills:\n");
		_appendSkills(sb);
		sb.append("\nRegistered PortalOps Tools:\n");
		_appendTools(sb);
		sb.append("\nCurrent Capabilities:\n");
		_appendCapabilities(sb);

		PortalOpsExecutionMetadata portalOpsExecutionMetadata =
			_lastExecutionMetadata.get();

		if (portalOpsExecutionMetadata != null) {
			sb.append("\nMost Recent PortalOps Execution:\n");
			sb.append("Execution Path:\n");

			for (String executionPathStep :
					portalOpsExecutionMetadata.getExecutionPath()) {

				sb.append("* ");
				sb.append(executionPathStep);
				sb.append("\n");
			}

			sb.append("Findings:\n");

			for (String finding : portalOpsExecutionMetadata.getFindings()) {
				sb.append("* ");
				sb.append(finding);
				sb.append("\n");
			}

			sb.append("Summary: ");
			sb.append(portalOpsExecutionMetadata.getSummary());
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String getSystemPrompt() {
		return StringBundler.concat(
			"You are PortalOps Assistant.\n\n",
			"PortalOps is an AI operations platform for Liferay.\n\n",
			"Always prefer PortalOps runtime metadata over general Liferay knowledge.\n\n",
			"When users refer to agents, skills, tools, capabilities, findings, recommendations, execution paths, or PortalOps functionality, assume they are referring to PortalOps concepts unless explicitly stated otherwise.\n\n",
			"PortalOps runtime metadata is authoritative.\n\n",
			"Do not invent PortalOps Agents, Skills, or Tools.\n\n",
			"If PortalOps does not support a capability, clearly state that the capability is not currently implemented.\n\n",
			"When runtime metadata is available, use it before relying on general Liferay knowledge.");
	}

	@Override
	public void recordExecution(
		PortalOpsExecutionMetadata portalOpsExecutionMetadata) {

		_lastExecutionMetadata.set(portalOpsExecutionMetadata);
	}

	private void _appendAgents(StringBundler sb) {
		for (PortalOpsAgent portalOpsAgent : _getSorted(_portalOpsAgents)) {
			sb.append("* ");
			sb.append(portalOpsAgent.getName());
			sb.append("\n");
			sb.append("  Description: ");
			sb.append(portalOpsAgent.getDescription());
			sb.append("\n");
			sb.append("  Skills:\n");

			for (String supportedSkill : portalOpsAgent.getSupportedSkills()) {
				sb.append("  * ");
				sb.append(supportedSkill);
				sb.append("\n");
			}
		}
	}

	private void _appendCapabilities(StringBundler sb) {
		Set<String> capabilities = new LinkedHashSet<>();

		for (PortalOpsAgent portalOpsAgent : _getValues(_portalOpsAgents)) {
			capabilities.addAll(portalOpsAgent.getCapabilities());
		}

		for (PortalOpsSkill portalOpsSkill : _getValues(_portalOpsSkills)) {
			capabilities.addAll(portalOpsSkill.getCapabilities());
		}

		for (PortalOpsTool portalOpsTool : _getValues(_portalOpsTools)) {
			capabilities.addAll(portalOpsTool.getCapabilities());
		}

		for (String capability : capabilities) {
			sb.append("* ");
			sb.append(capability);
			sb.append("\n");
		}
	}

	private void _appendSkills(StringBundler sb) {
		for (PortalOpsSkill portalOpsSkill : _getSorted(_portalOpsSkills)) {
			sb.append("* ");
			sb.append(portalOpsSkill.getName());
			sb.append("\n");
			sb.append("  Description: ");
			sb.append(portalOpsSkill.getDescription());
			sb.append("\n");
			sb.append("  Example Prompts:\n");

			for (String examplePrompt : portalOpsSkill.getExamplePrompts()) {
				sb.append("  * ");
				sb.append(examplePrompt);
				sb.append("\n");
			}

			sb.append("  Tools:\n");

			for (String supportedTool : portalOpsSkill.getSupportedTools()) {
				sb.append("  * ");
				sb.append(supportedTool);
				sb.append("\n");
			}
		}
	}

	private void _appendTools(StringBundler sb) {
		for (PortalOpsTool portalOpsTool : _getSorted(_portalOpsTools)) {
			sb.append("* ");
			sb.append(portalOpsTool.getName());
			sb.append("\n");
			sb.append("  Description: ");
			sb.append(portalOpsTool.getDescription());
			sb.append("\n");
		}
	}

	private <T> List<T> _getSorted(Collection<T> collection) {
		List<T> values = new ArrayList<>(_getValues(collection));

		values.sort(
			Comparator.comparing(
				value -> {
					if (value instanceof PortalOpsAgent) {
						return ((PortalOpsAgent)value).getName();
					}

					if (value instanceof PortalOpsSkill) {
						return ((PortalOpsSkill)value).getName();
					}

					return ((PortalOpsTool)value).getName();
				}));

		return values;
	}

	private <T> Collection<T> _getValues(Collection<T> collection) {
		if (collection == null) {
			return List.of();
		}

		return collection;
	}

	private final AtomicReference<PortalOpsExecutionMetadata>
		_lastExecutionMetadata = new AtomicReference<>();

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsAgent> _portalOpsAgents;

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsSkill> _portalOpsSkills;

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	private volatile Collection<PortalOpsTool> _portalOpsTools;

}
