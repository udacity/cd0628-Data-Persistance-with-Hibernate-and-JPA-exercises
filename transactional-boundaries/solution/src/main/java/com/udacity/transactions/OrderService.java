package com.udacity.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired private PaymentGateway paymentGateway;

    // @Transactional wraps the whole method atomically. If charge()
    // throws PaymentDeclinedException (a RuntimeException), the order
    // save and inventory decrement roll back automatically. The audit
    // row stays because recordEvent ran in REQUIRES_NEW.
    @Transactional
    public Order placeOrder(Order order) {
        // 1. Save the order first so it has an id for the audit message
        Order saved = orderRepository.save(order);

        // 2. Decrement product inventory
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new IllegalStateException(
                        "Product not found: " + order.getProductId()));
        product.setInventory(product.getInventory() - order.getQuantity());
        productRepository.save(product);

        // 3. Audit BEFORE payment so the attempt is recorded even on decline
        auditLogService.recordEvent(
                "PAYMENT_ATTEMPT",
                "Attempting payment for order " + saved.getId());

        // 4. Charge -- throws PaymentDeclinedException on DECLINE
        paymentGateway.charge(saved);

        return saved;
    }
}