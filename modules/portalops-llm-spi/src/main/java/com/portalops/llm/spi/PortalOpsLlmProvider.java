package com.portalops.llm.spi;

import com.portalops.api.knowledge.PortalKnowledgeSnapshot;

public interface PortalOpsLlmProvider {

    public String summarize(String prompt, PortalKnowledgeSnapshot snapshot);

}