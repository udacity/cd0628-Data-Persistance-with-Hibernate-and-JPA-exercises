package com.udacity.transactions;

/**
 * Thrown by PaymentGateway when the simulated charge is DECLINED.
 * As a RuntimeException, it triggers @Transactional rollback.
 */
public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(String message) {
        super(message);
    }
}