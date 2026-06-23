package com.udacity.datajpa;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable Specification builders for Product queries.
 * Each returns null at the predicate level when the input is null,
 * which Spring Data safely drops from the WHERE clause.
 */
public class ProductSpecs {

    private ProductSpecs() {}

    public static Specification<Product> categoryIs(String category) {
        return (root, query, cb) -> {
            if (category == null) return null;
            return cb.equal(root.get("category"), category);
        };
    }

    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) {
                return cb.between(root.get("price"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            }
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<Product> isInStock() {
        return (root, query, cb) -> cb.isTrue(root.get("inStock"));
    }
}