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
            deflt = "Answer only what the user asked. Minimize personal data exposure. Include individual user details only when explicitly requested. Do not offer actions or exports unless they are listed in PortalOps runtime capabilities.",
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
