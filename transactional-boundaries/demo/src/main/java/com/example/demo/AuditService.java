package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Audit logging in its own transaction so a failing outer transaction cannot
 * roll back the audit entry. Propagation.REQUIRES_NEW suspends the caller's
 * transaction, runs this method in a brand new one, and commits it
 * independently.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditRepo;

    public AuditService(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String action, String detail) {
        auditRepo.save(new AuditLog(action, detail));
        System.out.println("  [AUDIT written in its own transaction] " + action + ": " + detail);
    }
}
