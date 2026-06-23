package com.udacity.datajpa;

import java.math.BigDecimal;

/**
 * Interface projection: Spring Data generates a SELECT that returns
 * only these three columns, not the full Product entity.
 */
public interface ProductSummary {
    Long getId();
    String getName();
    BigDecimal getPrice();
}