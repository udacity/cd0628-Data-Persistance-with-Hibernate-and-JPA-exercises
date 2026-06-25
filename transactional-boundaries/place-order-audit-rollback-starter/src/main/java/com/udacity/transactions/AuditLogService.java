package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // ============================================================
    // TODO 1: Annotate this method so it runs in a NEW, INDEPENDENT
    //         transaction, even when called from inside an existing
    //         one.
    //
    // The goal: when OrderService.placeOrder rolls back, the audit
    // row written here should NOT roll back with it. The audit
    // record is evidence the attempt was made, and we want it to
    // persist even on failure.
    //
    // What you need:
    //   - The right @Transactional annotation
    //   - The right propagation setting that suspends the caller's
    //     transaction and starts a fresh one for this method
    //
    // CRITICAL:
    //   - Use Spring's @Transactional from
    //     org.springframework.transaction.annotation
    //   - NOT jakarta.transaction.Transactional - the Jakarta
    //     version does not honor Spring's propagation settings,
    //     so the audit row would silently roll back with the caller
    // ============================================================
    public void recordEvent(String eventType, String description) {
        auditLogRepository.save(new AuditLog(eventType, description));
    }
}