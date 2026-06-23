package com.udacity.nativequeries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ============================================================
    // TODO 1: Implement using a native SQL query.
    //
    // Annotate the method below with @Query:
    //
    //   @Query(value = """
    //       SELECT date_trunc('month', placed_at) AS month,
    //              category                       AS category,
    //              SUM(amount)                    AS total
    //       FROM orders
    //       GROUP BY date_trunc('month', placed_at), category
    //       ORDER BY month, category
    //       """,
    //       nativeQuery = true)
    //
    // Notes:
    //   - nativeQuery=true tells Hibernate not to parse as JPQL
    //   - date_trunc is PostgreSQL-only; JPQL has no equivalent
    //   - Column aliases (AS month, AS category, AS total) match
    //     the @SqlResultSetMapping declared on the Order entity
    //
    // The actual mapped query is executed in QueryTest via
    // EntityManager.createNativeQuery(...,"MonthlyRevenueMapping").
    // This raw-Object[] version is here for spot-checking shape.
    // ============================================================
    List<Object[]> findMonthlyRevenueByCategoryRaw();
}