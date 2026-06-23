package com.udacity.nativequeries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Native SQL aggregation: date_trunc is PostgreSQL-only so JPQL
    // won't work. Column aliases match the @SqlResultSetMapping
    // declared on the Order entity. This Object[] version is for
    // convenience; the test executes the same query via
    // EntityManager.createNativeQuery(..., "MonthlyRevenueMapping").
    @Query(value = "SELECT date_trunc('month', placed_at) AS month, " +
                   "category AS category, " +
                   "SUM(amount) AS total " +
                   "FROM orders " +
                   "GROUP BY date_trunc('month', placed_at), category " +
                   "ORDER BY month, category",
           nativeQuery = true)
    List<Object[]> findMonthlyRevenueByCategoryRaw();
}