package com.udacity.datajpa;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable Specification builders for Product queries.
 * Each one returns a single predicate that can be combined with .and(...).
 */
public class ProductSpecs {

    private ProductSpecs() {}

    // ============================================================
    // TODO 4: Implement categoryIs(String category).
    //
    // Return a Specification that adds an equality predicate for the
    // category field. If category is null, return a no-op spec:
    //
    //   return (root, query, cb) -> {
    //       if (category == null) return null;
    //       return cb.equal(root.get("category"), category);
    //   };
    //
    // The no-op return null at the predicate level is safe when this
    // spec is composed with .and(...) -- Spring Data drops null
    // predicates from the WHERE clause.
    // ============================================================
    public static Specification<Product> categoryIs(String category) {
        return (root, query, cb) -> null; // placeholder
    }

    // ============================================================
    // TODO 5: Implement priceBetween(BigDecimal min, BigDecimal max).
    //
    // Return a Specification that adds a between predicate for price.
    // Handle nullable bounds:
    //
    //   return (root, query, cb) -> {
    //       if (min == null && max == null) return null;
    //       if (min != null && max != null) {
    //           return cb.between(root.get("price"), min, max);
    //       }
    //       if (min != null) {
    //           return cb.greaterThanOrEqualTo(root.get("price"), min);
    //       }
    //       return cb.lessThanOrEqualTo(root.get("price"), max);
    //   };
    // ============================================================
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> null; // placeholder
    }

    // ============================================================
    // TODO 6: Implement isInStock().
    //
    // Fixed predicate, no parameters:
    //
    //   return (root, query, cb) -> cb.isTrue(root.get("inStock"));
    // ============================================================
    public static Specification<Product> isInStock() {
        return (root, query, cb) -> null; // placeholder
    }
}