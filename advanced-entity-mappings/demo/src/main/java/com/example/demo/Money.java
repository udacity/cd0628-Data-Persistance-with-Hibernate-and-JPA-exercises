package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * A value type for money. Marked @Embeddable so JPA can embed its fields
 * directly into any owning entity. Not an entity itself, has no id, no
 * lifecycle of its own.
 */
@Embeddable
public class Money {

    @Column(name = "price_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "price_currency", nullable = false, length = 3)
    private String currency;

    protected Money() {}

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
