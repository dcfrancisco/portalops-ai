package com.portalops.assistant.api;

public interface PortalOpsContextProvider {

	public String buildRuntimeContext();

	public String getSystemPrompt();

	public void recordExecution(PortalOpsExecutionMetadata portalOpsExecutionMetadata);

}
