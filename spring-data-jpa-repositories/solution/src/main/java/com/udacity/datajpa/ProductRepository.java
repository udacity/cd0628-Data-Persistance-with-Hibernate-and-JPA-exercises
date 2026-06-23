package com.udacity.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

// JpaSpecificationExecutor adds findAll(Specification) for the
// Specification-based search. Without it Specifications silently do
// nothing because the method literally doesn't exist on the interface.
public interface ProductRepository
        extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

    // Derived method: Spring Data parses the name into a WHERE clause
    // (category = ? AND price < ?). No annotation, no body required.
    List<Product> findByCategoryAndPriceLessThan(String category, BigDecimal maxPrice);

    // @Query for the join + aggregate. Derived methods can't express
    // GROUP BY + ORDER BY on an aggregate.
    @Query("""
            SELECT p FROM Product p
            JOIN OrderItem oi ON oi.product = p
            WHERE EXTRACT(YEAR FROM oi.soldAt) = :year
              AND EXTRACT(MONTH FROM oi.soldAt) = :month
            GROUP BY p.id
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<Product> findTopSellersByMonth(@Param("year") int year, @Param("month") int month);

    // Interface projection: returning ProductSummary tells Spring Data
    // to generate a narrower SELECT (id, name, price only). Check the
    // SQL log to confirm only three columns are selected.
    List<ProductSummary> findSummariesByCategory(String category);
}