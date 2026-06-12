package com.portalops.llm.spi;

import com.portalops.ai.api.AIRequest;
import com.portalops.ai.api.AIResponse;

public interface AIProvider {

    public AIResponse complete(AIRequest aiRequest);

    public String getProviderType();

}
