package com.udacity.datajpa;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable Specification builders for Product queries.
 * Each method returns a single Specification that can be combined
 * via Specification.allOf(...) or .and(...).
 */
public class ProductSpecs {

    private ProductSpecs() {}

    // ============================================================
    // TODO 4: Build a Specification that filters by category.
    //
    // The goal: return a Specification<Product> that, when applied,
    // adds an equality predicate on the category field.
    //
    // Behavior when category is null:
    //   - Return a "no-op" Specification by having the lambda
    //     return null at the predicate level. Spring Data drops
    //     null predicates from the composed WHERE clause, which
    //     is exactly what you want when the filter is absent.
    //
    // Hints:
    //   - A Specification is a lambda of (root, query, cb) -> Predicate
    //   - Use cb.equal(path, value) to build an equality predicate
    //   - root.get("fieldName") gets a Path to a field
    // ============================================================
    public static Specification<Product> categoryIs(String category) {
        return null; // replace with your Specification lambda
    }

    // ============================================================
    // TODO 5: Build a Specification for an inclusive price range.
    //
    // The goal: return a Specification that filters by price, but
    // gracefully handles each combination of nulls:
    //   - both null    -> no predicate (return null inside the lambda)
    //   - only min set -> price >= min
    //   - only max set -> price <= max
    //   - both set     -> between (inclusive)
    //
    // Hints:
    //   - cb.between(path, lo, hi)
    //   - cb.greaterThanOrEqualTo(path, value)
    //   - cb.lessThanOrEqualTo(path, value)
    // ============================================================
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return null; // replace with your Specification lambda
    }

    // ============================================================
    // TODO 6: Build a Specification that filters to in-stock items.
    //
    // The goal: return a Specification that adds a predicate
    // checking the inStock boolean is true. No parameters needed.
    //
    // Hints:
    //   - cb.isTrue(path) is the cleanest expression
    //   - root.get("inStock") returns the Path to the boolean field
    // ============================================================
    public static Specification<Product> isInStock() {
        return null; // replace with your Specification lambda
    }
}