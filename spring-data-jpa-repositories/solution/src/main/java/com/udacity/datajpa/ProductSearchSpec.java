package com.udacity.datajpa;

import java.math.BigDecimal;

/**
 * Optional filters for Specification-based search.
 * Any null field is skipped when composing the Specification.
 */
public record ProductSearchSpec(
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean onlyInStock
) {
}