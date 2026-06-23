package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // REQUIRES_NEW: suspends the caller's transaction and starts a
    // fresh one for this method. The new transaction commits
    // independently, so the audit row survives even when the caller
    // later rolls back.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordEvent(String eventType, String description) {
        auditLogRepository.save(new AuditLog(eventType, description));
    }
}