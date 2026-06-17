package com.udacity.relationships;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the four relationships:
 *   - User <-> UserProfile bidirectional @OneToOne with shared PK
 *   - Order -> Customer @ManyToOne LAZY
 *
 * The SQL log (enabled in application.yml) shows:
 *   - A single INSERT cascade when saving a User with its UserProfile
 *   - N+1 query pattern when iterating Orders without JOIN FETCH
 *   - A single query when using findAllWithCustomer
 */
@SpringBootTest(classes = Application.class)
@Transactional
class RelationshipTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void userProfile_cascadeSaveAndSharedPrimaryKey() {
        User user = new User("alice", "alice@example.com");
        UserProfile profile = new UserProfile("Alice A.", "Curious learner.");
        profile.setUser(user);
        user.setProfile(profile);

        // Saving the User cascades to UserProfile (single transaction)
        em.persist(user);
        em.flush();

        // Shared primary key: user.id == profile.id
        assertThat(user.getId()).isNotNull();
        assertThat(profile.getId()).isEqualTo(user.getId());
    }

    @Test
    void orderToCustomer_lazyVsJoinFetch() {
        // Load 3 orders (seeded by data.sql) without JOIN FETCH.
        // LAZY means accessing customer.getName() fires a separate
        // SELECT for each one - the N+1 problem.
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isNotEmpty();
        for (Order order : orders) {
            // Each .getName() call may trigger a SQL query
            assertThat(order.getCustomer().getName()).isNotNull();
        }

        em.clear(); // wipe persistence context to force fresh loads

        // Same data with JOIN FETCH - one SQL query for all Orders + Customers
        List<Order> ordersWithCustomers = orderRepository.findAllWithCustomer();
        assertThat(ordersWithCustomers).isNotEmpty();
        for (Order order : ordersWithCustomers) {
            assertThat(order.getCustomer().getName()).isNotNull();
        }
    }
}