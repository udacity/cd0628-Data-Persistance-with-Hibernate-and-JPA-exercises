# Exercise: Embeddable Address, Inheritance, and a Money Converter

## What You'll Build

Apply three mapping patterns to a small e-commerce starter — extract
Address as @Embeddable, configure Payment with JOINED inheritance, and
finish a Money AttributeConverter. Each is a focused, isolated change.

## Requirements

- Address class annotated @Embeddable; Customer and Order use @Embedded
  (Order uses @AttributeOverrides to avoid column collision)
- Payment uses @Inheritance(strategy = JOINED); two subclasses
  (CreditCardPayment, BankTransferPayment) need @Entity annotations
- MoneyConverter implements AttributeConverter<Money, BigDecimal> with
  autoApply = true — fill in the two conversion methods
- The provided SchemaInspectionTest verifies the generated DDL

## Starter Code

- PostgreSQL with Hibernate DDL generation on
- Customer, Order, Payment hierarchy, Address, Money — all fields complete
- TODOs for: @Embeddable on Address, @Embedded on Customer/Order,
  @AttributeOverrides on Order, @Inheritance on Payment, @Entity on the
  two subclasses, @Converter(autoApply=true) on MoneyConverter, plus
  the two conversion methods
- SchemaInspectionTest pre-written

## Verification

- All three SchemaInspectionTest methods pass
- Address columns appear inline on customer and order tables
- JOINED inheritance generates the parent + two subtype tables
- Money columns are amount + currency wherever used

## Common Mistakes

- @Embeddable shared without @AttributeOverrides — column collisions
- SINGLE_TABLE inheritance for sparse subtypes
- AttributeConverter that doesn't handle null
- Forgetting autoApply = true