package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired private PaymentGateway paymentGateway;

    // ============================================================
    // TODO 2: Annotate this method as transactional.
    //
    // The goal: the four steps inside placeOrder should be ALL-OR-
    // NOTHING. If the payment gateway throws, the order save and
    // inventory decrement should both roll back automatically.
    //
    // Hint:
    //   - Use Spring's @Transactional, not Jakarta's
    //
    //
    // TODO 3: Implement placeOrder.
    //
    // The method receives an Order with productId, quantity, and
    // totalAmount already populated. You need to:
    //
    //   1. Persist the order so it has an id
    //   2. Look up the matching Product, decrement its inventory by
    //      the order's quantity, persist the change
    //   3. Record an audit event for the payment ATTEMPT, including
    //      the order id - this must happen BEFORE the payment call
    //      so the audit row exists even if payment declines
    //   4. Charge the payment gateway with the order
    //
    // The order of step 3 vs step 4 matters: if you audit AFTER the
    // payment, the audit only fires on the happy path, defeating
    // the whole point of recording the attempt.
    //
    // Why the rollback works:
    //   - PaymentGateway.charge throws PaymentDeclinedException, a
    //     RuntimeException
    //   - @Transactional rolls back automatically on RuntimeException
    //   - The audit row written in step 3 survives because
    //     AuditLogService uses a separate, independent transaction
    //
    // Pitfalls:
    //   - Don't try-catch the exception inside this method. Let it
    //     propagate so @Transactional sees it and rolls back
    //   - Use productRepository.findById(...).orElseThrow(...) and
    //     fail fast if the product isn't found
    // ============================================================
    public Order placeOrder(Order order) {
        return null; // placeholder
    }
}