package com.udacity.relationships;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Pre-written JOIN FETCH query: loads Orders WITH their Customers
     * in a single SQL statement. Compare this to findAll() which
     * triggers N+1 queries because Customer is LAZY.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.customer")
    List<Order> findAllWithCustomer();
}