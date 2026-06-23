package com.udacity.nativequeries;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for the monthly revenue native query result.
 * Constructor matches the order of columns in the @SqlResultSetMapping
 * declared on the Order entity.
 */
public class MonthlyRevenueReport {

    private final LocalDateTime month;
    private final String category;
    private final BigDecimal total;

    public MonthlyRevenueReport(LocalDateTime month, String category, BigDecimal total) {
        this.month = month;
        this.category = category;
        this.total = total;
    }

    public LocalDateTime getMonth() { return month; }
    public String getCategory() { return category; }
    public BigDecimal getTotal() { return total; }
}