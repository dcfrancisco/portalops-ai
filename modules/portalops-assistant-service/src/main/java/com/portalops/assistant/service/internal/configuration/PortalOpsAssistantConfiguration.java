package com.portalops.assistant.service.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
        category = "portalops",
        scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(name = "PortalOps Assistant")
public interface PortalOpsAssistantConfiguration {

    @Meta.AD(
            deflt = "",
            description = "Additional instructions appended to the required PortalOps system prompt.",
            name = "Additional System Prompt",
            required = false
    )
    public String additionalSystemPrompt();

    @Meta.AD(
            deflt = "OPENAI",
            name = "Provider Type"
    )
    public String providerType();

}
