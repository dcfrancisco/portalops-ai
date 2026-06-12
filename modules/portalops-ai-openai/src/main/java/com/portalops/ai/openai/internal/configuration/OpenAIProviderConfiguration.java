package com.portalops.ai.openai.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
        category = "portalops",
        scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(name = "PortalOps OpenAI Provider")
public interface OpenAIProviderConfiguration {

    @Meta.AD(
            deflt = "",
            name = "API Key",
            required = false,
            type = Meta.Type.Password
    )
    public String apiKey();

    @Meta.AD(deflt = "gpt-5-mini", name = "Model Name")
    public String modelName();

}
