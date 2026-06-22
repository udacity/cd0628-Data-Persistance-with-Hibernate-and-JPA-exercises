package com.udacity.mappings;

import jakarta.persistence.AttributeConverter;

import java.math.BigDecimal;

// ============================================================
// TODO 8: Annotate this class with:
//   @Converter(autoApply = true)
//
// autoApply=true means Hibernate uses this converter automatically
// for any Money field. Without it you'd have to add @Convert on
// every Money attribute.
// ============================================================
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        // ============================================================
        // TODO 9: Return the BigDecimal amount from the Money object.
        // Handle null gracefully -- return null if input is null.
        //
        // Example:
        //   return money == null ? null : money.getAmount();
        // ============================================================
        return null;
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal value) {
        // ============================================================
        // TODO 10: Build and return a Money object from the BigDecimal.
        // Default currency to "USD". Handle null gracefully.
        //
        // Example:
        //   return value == null ? null : new Money(value, "USD");
        // ============================================================
        return null;
    }
}