package com.udacity.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Optional filters for dynamic order search.
 * Any null field is skipped when building the Criteria query.
 */
public record OrderSearchCriteria(
        String status,
        Long customerId,
        BigDecimal minAmount,
        LocalDateTime fromDate,
        LocalDateTime toDate
) {
}