package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired private PaymentGateway paymentGateway;

    // ============================================================
    // TODO 2: Annotate this method with @Transactional
    //
    // Import:
    //   import org.springframework.transaction.annotation.Transactional;
    //
    // Wraps the whole method in a single atomic transaction. If
    // anything throws a RuntimeException, all DB changes here roll
    // back automatically.
    //
    //
    // TODO 3: Implement the method body in order:
    //   1. Save the Order via orderRepository.save(order)
    //   2. Find the Product by order.getProductId(), decrement its
    //      inventory by order.getQuantity(), save it
    //   3. Call auditLogService.recordEvent("PAYMENT_ATTEMPT",
    //         "Attempting payment for order " + order.getId())
    //      BEFORE the payment call so the audit row exists even if
    //      payment then declines
    //   4. Call paymentGateway.charge(order)
    //
    // If charge throws PaymentDeclinedException, @Transactional rolls
    // back the order save and inventory decrement. The audit row stays
    // because recordEvent ran in REQUIRES_NEW.
    // ============================================================
    public Order placeOrder(Order order) {
        return null; // placeholder
    }
}