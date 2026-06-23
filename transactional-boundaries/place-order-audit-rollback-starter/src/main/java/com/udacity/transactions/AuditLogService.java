package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // ============================================================
    // TODO 1: Annotate this method with:
    //   @Transactional(propagation = Propagation.REQUIRES_NEW)
    //
    // Required imports:
    //   import org.springframework.transaction.annotation.Transactional;
    //   import org.springframework.transaction.annotation.Propagation;
    //
    // REQUIRES_NEW suspends any existing transaction and starts a new
    // one for this method. The new transaction commits independently --
    // so even when the caller's transaction rolls back, the audit row
    // it inserted here remains in the database.
    //
    // IMPORTANT: use Spring's @Transactional from
    //   org.springframework.transaction.annotation
    // NOT jakarta.transaction.Transactional -- the Jakarta version
    // does not honor Spring's propagation settings.
    // ============================================================
    public void recordEvent(String eventType, String description) {
        auditLogRepository.save(new AuditLog(eventType, description));
    }
}