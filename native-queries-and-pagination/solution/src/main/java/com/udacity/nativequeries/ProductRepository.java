package com.udacity.nativequeries;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Keyset pagination: WHERE name > :cursor walks past the last
    // result. ORDER BY name, id ensures a deterministic cursor even
    // when two products share a name. No offset = no skip cost,
    // so this stays fast regardless of page number.
    @Query("SELECT p FROM Product p " +
           "WHERE p.name > :lastName " +
           "ORDER BY p.name ASC, p.id ASC")
    List<Product> findProductsAfter(@Param("lastName") String lastName, Limit limit);
}