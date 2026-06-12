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
            deflt = "OPENAI",
            name = "Provider Type"
    )
    public String providerType();

}
