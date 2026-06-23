package com.udacity.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Static JPQL aggregation. Constructor expression projects directly
    // into TopCustomerDto so we don't load full Customer entities for a
    // reporting query. GROUP BY both selected non-aggregated columns.
    @Query("""
            SELECT new com.udacity.report.TopCustomerDto(
                c.id, c.name, SUM(o.amount))
            FROM Order o
            JOIN o.customer c
            WHERE EXTRACT(YEAR FROM o.placedAt) = :year
            GROUP BY c.id, c.name
            ORDER BY SUM(o.amount) DESC
            LIMIT 10
            """)
    List<TopCustomerDto> findTopCustomers(@Param("year") int year);
}