package com.portalops.agent.management.skill;

import com.portalops.api.runtime.PortalOpsSkill;

public interface Skill extends PortalOpsSkill {

	public Object execute(String prompt);

}
