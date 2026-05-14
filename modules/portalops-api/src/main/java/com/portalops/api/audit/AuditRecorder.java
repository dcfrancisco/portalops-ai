package com.portalops.api.audit;

public interface AuditRecorder {

    public void record(AuditRecord auditRecord);

}