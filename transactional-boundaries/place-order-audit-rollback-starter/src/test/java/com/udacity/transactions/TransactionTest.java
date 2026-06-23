package com.udacity.transactions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies the placeOrder service behaves atomically AND that the
 * audit log call survives rollback via Propagation.REQUIRES_NEW.
 *
 * NOTE: tests are NOT @Transactional - we need real commits and
 * rollbacks visible across calls.
 */
@SpringBootTest(classes = Application.class)
class TransactionTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired private OrderService orderService;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private PaymentGateway paymentGateway;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void happyPath_orderSavedInventoryDecrementedAuditExists() {
        paymentGateway.setMode(PaymentGateway.Mode.APPROVE);

        Product before = productRepository.findById(1L).orElseThrow();
        int startInventory = before.getInventory();
        long auditCountBefore = auditLogRepository.count();

        Order order = new Order(1L, 2, new BigDecimal("19.98"));
        Order saved = orderService.placeOrder(order);

        assertThat(saved.getId()).isNotNull();

        Product after = productRepository.findById(1L).orElseThrow();
        assertThat(after.getInventory()).isEqualTo(startInventory - 2);

        long auditCountAfter = auditLogRepository.count();
        assertThat(auditCountAfter).isEqualTo(auditCountBefore + 1);

        // Cleanup so other tests have a known baseline
        orderRepository.deleteById(saved.getId());
        after.setInventory(startInventory);
        productRepository.save(after);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void paymentDeclined_orderRollsBackButAuditSurvives() {
        paymentGateway.setMode(PaymentGateway.Mode.DECLINE);

        Product before = productRepository.findById(2L).orElseThrow();
        int startInventory = before.getInventory();
        long orderCountBefore = orderRepository.count();
        long auditCountBefore = auditLogRepository.count();

        Order order = new Order(2L, 3, new BigDecimal("44.97"));

        assertThatThrownBy(() -> orderService.placeOrder(order))
                .isInstanceOf(PaymentDeclinedException.class);

        // Order rolled back
        assertThat(orderRepository.count()).isEqualTo(orderCountBefore);

        // Inventory rolled back
        Product after = productRepository.findById(2L).orElseThrow();
        assertThat(after.getInventory()).isEqualTo(startInventory);

        // Audit row survived
        assertThat(auditLogRepository.count()).isEqualTo(auditCountBefore + 1);

        // Reset gateway for other tests
        paymentGateway.setMode(PaymentGateway.Mode.APPROVE);
    }
}