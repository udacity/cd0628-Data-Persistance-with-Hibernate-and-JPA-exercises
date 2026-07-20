package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Two transferService methods that show three transaction concepts:
 *
 *   1. Default @Transactional: one atomic unit. Both debits and credits commit
 *      together, or neither does.
 *
 *   2. Propagation.REQUIRES_NEW on auditService.record: the audit log commits
 *      in its own transaction, independent of the caller. When the outer
 *      transfer fails, the audit row is still written.
 *
 *   3. Unchecked exceptions trigger rollback. Runtime exceptions like
 *      IllegalStateException roll back automatically. That's the JPA
 *      default. Checked exceptions do NOT trigger rollback unless you say so
 *      with rollbackFor.
 */
@Service
public class TransferService {

    private final AccountRepository accounts;
    private final AuditService audit;

    public TransferService(AccountRepository accounts, AuditService audit) {
        this.accounts = accounts;
        this.audit = audit;
    }

    /** Happy path. All operations commit together. */
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = accounts.findById(fromId).orElseThrow();
        Account to   = accounts.findById(toId).orElseThrow();

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        audit.record("TRANSFER", "Sent " + amount + " from " + from.getOwner()
                + " to " + to.getOwner());
    }

    /**
     * Failure path. The audit runs BEFORE we throw so its REQUIRES_NEW
     * transaction commits. Then the outer transaction fails and the balance
     * changes are rolled back. Audit row survives.
     */
    @Transactional
    public void transferAndFail(Long fromId, Long toId, BigDecimal amount) {
        Account from = accounts.findById(fromId).orElseThrow();
        Account to   = accounts.findById(toId).orElseThrow();

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        audit.record("TRANSFER_ATTEMPT",
                "Attempted " + amount + " from " + from.getOwner()
                + " to " + to.getOwner());

        // Simulate a downstream failure AFTER the audit was written.
        throw new IllegalStateException("Simulated downstream failure");
    }
}
