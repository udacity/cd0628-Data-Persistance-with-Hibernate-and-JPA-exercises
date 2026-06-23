package com.udacity.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// ============================================================
// TODO 7: Add JpaSpecificationExecutor<Product> to extends clause.
//
// Change the line below from:
//   extends JpaRepository<Product, Long>
// to:
//   extends JpaRepository<Product, Long>,
//           JpaSpecificationExecutor<Product>
//
// Required import:
//   import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//
// Without this, findAll(Specification) won't compile and your
// Specification searches silently do nothing.
// ============================================================
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============================================================
    // TODO 1: Add a derived method signature.
    //
    // Spring Data generates the query from the method name alone.
    // No @Query annotation needed.
    //
    //   List<Product> findByCategoryAndPriceLessThan(String category,
    //                                                BigDecimal maxPrice);
    //
    // Spring Data parses "findBy" + "Category" + "And" + "PriceLessThan"
    // and writes the WHERE clause for you.
    // ============================================================


    // ============================================================
    // TODO 2: Add findTopSellersByMonth annotated with @Query.
    //
    //   @Query("""
    //       SELECT p FROM Product p
    //       JOIN OrderItem oi ON oi.product = p
    //       WHERE EXTRACT(YEAR FROM oi.soldAt) = :year
    //         AND EXTRACT(MONTH FROM oi.soldAt) = :month
    //       GROUP BY p.id
    //       ORDER BY SUM(oi.quantity) DESC
    //       """)
    //   List<Product> findTopSellersByMonth(@Param("year")  int year,
    //                                       @Param("month") int month);
    // ============================================================


    // ============================================================
    // TODO 3: Add findSummariesByCategory returning the projection.
    //
    //   List<ProductSummary> findSummariesByCategory(String category);
    //
    // Returning the ProductSummary interface tells Spring Data to
    // generate a narrower SELECT (id, name, price only). Check the
    // SQL log to confirm only three columns are selected.
    // ============================================================
}