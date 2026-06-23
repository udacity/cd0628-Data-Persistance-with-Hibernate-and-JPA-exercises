package com.udacity.report;

import java.math.BigDecimal;

/**
 * Projection DTO for the top-customers report.
 * Used via JPQL constructor expression.
 */
public record TopCustomerDto(Long customerId, String customerName, BigDecimal totalSpend) {
}