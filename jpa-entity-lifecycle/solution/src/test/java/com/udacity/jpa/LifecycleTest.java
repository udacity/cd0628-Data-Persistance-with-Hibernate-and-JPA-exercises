package com.udacity.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Walks Customer (IDENTITY id strategy) and Order (SEQUENCE id strategy)
 * through every JPA lifecycle state: transient, managed (via persist),
 * detached, managed again (via merge), and removed.
 *
 * Solution version - all tests pass against the completed entities.
 * The SQL log (enabled in application.yml) reveals the key difference:
 *   - IDENTITY: INSERT fires immediately on persist()
 *   - SEQUENCE: INSERT is deferred until flush()
 */
@SpringBootTest(classes = Application.class)
@Transactional
class LifecycleTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    void customerLifecycle_walksThroughEveryState() {
        // ---- TRANSIENT ----
        Customer customer = new Customer("Alice", "alice@example.com");
        assertThat(customer.getId()).isNull();
        assertThat(em.contains(customer)).isFalse();

        // ---- MANAGED (via persist) ----
        em.persist(customer);
        // With IDENTITY, the INSERT fires here and the id is populated
        assertThat(customer.getId()).isNotNull();
        assertThat(em.contains(customer)).isTrue();

        Long customerId = customer.getId();

        // ---- DETACHED ----
        em.detach(customer);
        assertThat(em.contains(customer)).isFalse();

        // Changes to a detached entity are NOT flushed automatically
        customer.setName("Alice Updated");

        // ---- MANAGED again (via merge) ----
        Customer mergedCustomer = em.merge(customer);
        assertThat(em.contains(mergedCustomer)).isTrue();
        assertThat(mergedCustomer.getName()).isEqualTo("Alice Updated");

        // ---- REMOVED ----
        em.remove(mergedCustomer);
        em.flush();
        assertThat(em.find(Customer.class, customerId)).isNull();
    }

    @Test
    void orderLifecycle_walksThroughEveryState() {
        // ---- TRANSIENT ----
        Order order = new Order("Widget order", 99.99);
        assertThat(order.getId()).isNull();
        assertThat(em.contains(order)).isFalse();

        // ---- MANAGED (via persist) ----
        em.persist(order);
        // With SEQUENCE, the id is assigned (from the sequence)
        // but the INSERT is deferred until flush
        assertThat(order.getId()).isNotNull();
        assertThat(em.contains(order)).isTrue();

        em.flush(); // <-- INSERT actually fires here for SEQUENCE strategy

        Long orderId = order.getId();

        // ---- DETACHED ----
        em.detach(order);
        assertThat(em.contains(order)).isFalse();

        order.setDescription("Updated order");

        // ---- MANAGED again (via merge) ----
        Order mergedOrder = em.merge(order);
        assertThat(em.contains(mergedOrder)).isTrue();
        assertThat(mergedOrder.getDescription()).isEqualTo("Updated order");

        // ---- REMOVED ----
        em.remove(mergedOrder);
        em.flush();
        assertThat(em.find(Order.class, orderId)).isNull();
    }
}