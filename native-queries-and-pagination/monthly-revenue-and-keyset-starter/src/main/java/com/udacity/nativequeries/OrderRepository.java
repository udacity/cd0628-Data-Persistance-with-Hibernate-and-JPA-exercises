package com.udacity.nativequeries;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ============================================================
    // TODO 1: Implement a native SQL aggregation that returns
    //         monthly revenue grouped by category.
    //
    // The goal: produce one row per (month, category) pair, with
    // the total order amount for that combination.
    //
    // Why native SQL instead of JPQL?
    //   - You need date_trunc('month', placed_at), a PostgreSQL
    //     function with no JPQL equivalent
    //
    // Your query needs to:
    //   - Truncate placed_at to month using date_trunc
    //   - Group by both that truncated month AND category
    //   - Sum the order amount
    //   - Order by month then category for deterministic output
    //
    // Hints on annotations:
    //   - @Query with nativeQuery = true tells Hibernate not to
    //     parse it as JPQL
    //   - Alias each SELECT column (AS month, AS category, AS total)
    //     so the @SqlResultSetMapping declared on the Order entity
    //     can map by name into MonthlyRevenueReport
    //
    // The actual mapped query is executed in QueryTest via
    // EntityManager.createNativeQuery(..., "MonthlyRevenueMapping").
    // This raw-Object[] version is a checkpoint -- once it returns
    // rows, the mapping wires up the typed DTO automatically.
    // ============================================================
    List<Object[]> findMonthlyRevenueByCategoryRaw();
}