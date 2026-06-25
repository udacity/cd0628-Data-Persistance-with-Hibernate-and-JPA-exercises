package com.udacity.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;

// ============================================================
// TODO 7: This interface currently extends only JpaRepository.
//
// To enable Specification-based search, you'll need it to also
// extend JpaSpecificationExecutor<Product>. Without that second
// interface, the findAll(Specification) method doesn't exist and
// your Specification queries will fail to compile.
//
// Look up which package JpaSpecificationExecutor lives in and add
// the appropriate import.
// ============================================================
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============================================================
    // TODO 1: Add a DERIVED query method.
    //
    // The goal: find all products in a given category that cost
    // less than a given price.
    //
    // Why derived?
    //   - Spring Data parses the method name and generates the
    //     WHERE clause for you - no body, no @Query needed
    //
    // Hints:
    //   - The method name must follow Spring Data's naming grammar:
    //     findBy + <Property> + <Operator> + And + <Property> + ...
    //   - "LessThan" is one of the supported operators
    //   - Return List<Product>
    //   - Spring Data will fail at startup with "No property xyz
    //     found" if your name doesn't match a real field
    // ============================================================


    // ============================================================
    // TODO 2: Add a method annotated with @Query.
    //
    // The goal: find the top-selling products for a given month,
    // ordered by total quantity sold descending.
    //
    // Why @Query instead of a derived method?
    //   - Derived methods can't express joins to other entities,
    //     aggregates, or grouping. This needs all three.
    //
    // Your query needs to:
    //   - Join Product to OrderItem on the relationship
    //   - Filter to a specific year and month using EXTRACT
    //   - Group by product (so SUM works per product)
    //   - Order by SUM(quantity) descending
    //
    // Hints:
    //   - The method takes year and month as ints
    //   - Use @Param for each
    //   - Return List<Product>
    // ============================================================


    // ============================================================
    // TODO 3: Add a method returning the ProductSummary projection.
    //
    // The goal: fetch a category's products but materialize only
    // id, name, and price rather than the full Product entity.
    //
    // Why a projection?
    //   - When you only need a few fields, returning the projection
    //     interface tells Spring Data to generate a narrower SELECT
    //     clause. Check the SQL log to confirm only three columns
    //     are selected
    //
    // Hints:
    //   - The method name uses the same derived-query grammar as
    //     TODO 1 (findBy...)
    //   - The return type is what triggers the projection: use
    //     List<ProductSummary>, not List<Product>
    // ============================================================
}