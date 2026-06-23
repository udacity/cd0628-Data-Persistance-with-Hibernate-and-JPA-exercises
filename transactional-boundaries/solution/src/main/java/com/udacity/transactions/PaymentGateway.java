package com.udacity.transactions;

import org.springframework.stereotype.Component;

/**
 * Simulated payment gateway. Toggle the mode field for testing:
 *   APPROVE - charge() returns silently
 *   DECLINE - charge() throws PaymentDeclinedException
 */
@Component
public class PaymentGateway {

    public enum Mode { APPROVE, DECLINE }

    private Mode mode = Mode.APPROVE;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void charge(Order order) {
        if (mode == Mode.DECLINE) {
            throw new PaymentDeclinedException(
                    "Simulated decline for order " + order.getId());
        }
        // APPROVE: silent success
    }
}