package com.udacity.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ============================================================
    // TODO 1: Implement using a JPQL @Query annotation.
    //
    // The goal: return the top 10 customers by total spend in a
    // given year, projected directly into TopCustomerDto so the
    // query doesn't materialize full Customer entities.
    //
    // Your query will need to:
    //   - Join Order to Customer
    //   - Filter by year on the order's placedAt timestamp
    //   - Group by the customer fields you select
    //   - Aggregate the order amounts with SUM
    //   - Order by total spend, descending
    //   - Limit to 10 rows
    //   - Project into TopCustomerDto via a JPQL constructor expression
    //
    // Hints:
    //   - JPQL constructor expression uses the FULLY QUALIFIED class
    //     name: new com.udacity.report.TopCustomerDto(...)
    //   - To filter by year, JPQL supports EXTRACT(YEAR FROM ...)
    //   - When you SELECT a non-aggregated column, it must appear in
    //     GROUP BY
    //   - JPQL supports LIMIT directly since JPA 3.2
    // ============================================================
    List<TopCustomerDto> findTopCustomers(@Param("year") int year);
}