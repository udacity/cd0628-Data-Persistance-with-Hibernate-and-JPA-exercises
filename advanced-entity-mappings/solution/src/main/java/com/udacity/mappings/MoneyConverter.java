package com.udacity.mappings;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

// autoApply=true means Hibernate uses this converter automatically
// for any Money field. Without it you'd need @Convert on each field.
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        // Defensive null handling: column may be nullable.
        return money == null ? null : money.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal value) {
        // Default currency to USD when reconstituting from the DB.
        return value == null ? null : new Money(value, "USD");
    }
}