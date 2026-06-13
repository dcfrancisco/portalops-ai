package com.portalops.assistant.api;

public interface PortalOpsContextProvider {

	public String buildRuntimeContext(
		PortalOpsExecutionMetadata portalOpsExecutionMetadata);

	public String getSystemPrompt();

}
