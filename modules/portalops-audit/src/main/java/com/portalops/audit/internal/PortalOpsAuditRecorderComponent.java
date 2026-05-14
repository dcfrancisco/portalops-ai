package com.portalops.audit.internal;

import com.portalops.api.audit.AuditRecord;
import com.portalops.api.audit.AuditRecorder;

import org.osgi.service.component.annotations.Component;

@Component(service = AuditRecorder.class)
public class PortalOpsAuditRecorderComponent implements AuditRecorder {

    @Override
    public void record(AuditRecord auditRecord) {
    }

}