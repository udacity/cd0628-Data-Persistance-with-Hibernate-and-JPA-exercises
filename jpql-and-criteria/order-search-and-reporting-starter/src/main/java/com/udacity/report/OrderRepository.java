package com.udacity.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ============================================================
    // TODO 1: Implement using a JPQL @Query.
    //
    // Annotate this method with:
    //
    //   @Query("""
    //     SELECT new com.udacity.report.TopCustomerDto(
    //         c.id, c.name, SUM(o.amount))
    //     FROM Order o
    //     JOIN o.customer c
    //     WHERE EXTRACT(YEAR FROM o.placedAt) = :year
    //     GROUP BY c.id, c.name
    //     ORDER BY SUM(o.amount) DESC
    //     LIMIT 10
    //     """)
    //
    // Notes:
    //   - Constructor expression projects directly into TopCustomerDto
    //   - GROUP BY every non-aggregated column you SELECT
    //   - EXTRACT(YEAR FROM <date>) filters by year
    //   - LIMIT 10 caps the result set
    // ============================================================
    List<TopCustomerDto> findTopCustomers(@Param("year") int year);
}