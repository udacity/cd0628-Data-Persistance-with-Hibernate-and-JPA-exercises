package com.udacity.nativequeries;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============================================================
    // TODO 2: Implement keyset pagination.
    //
    // Annotate this method with @Query:
    //
    //   @Query("SELECT p FROM Product p " +
    //          "WHERE p.name > :lastName " +
    //          "ORDER BY p.name ASC, p.id ASC")
    //
    // Notes:
    //   - name > :lastName walks past the cursor
    //   - id is a tiebreaker if two products share a name
    //   - Limit is a Spring Data 3.2+ alternative to Pageable
    //     when you only need a row cap, no offset
    // ============================================================
    List<Product> findProductsAfter(@Param("lastName") String lastName, Limit limit);
}