# Exercise: Map Address, Payment, and Money for an Order System

## Overview

In this exercise, you'll go beyond simple entities and model three
advanced mapping patterns: `@Embeddable` value types for Address,
JOINED inheritance for a Payment hierarchy with two subtypes
(CreditCard and BankTransfer), and a custom `AttributeConverter`
to persist a Money value object as two columns. You'll verify
the schema is shaped correctly and that the inheritance dispatches
to the right table on read.

## Exercise Instructions

Open the starter project and work through the TODOs across four
files.

### Part 1: Address as @Embeddable

Open `Address.java`.

**TODO 1: Annotate the class with `@Embeddable`**
Embeddable types are value objects -- they don't have an id of
their own, they live inside an owning entity's row.

Open `Customer.java`.

**TODO 2: Annotate the address field with `@Embedded`**
Marks the field as a value composition. The address columns will be
inlined into the customer table.

### Part 2: Payment Inheritance (JOINED)

Open `Payment.java`.

**TODO 3: Annotate as `@Entity` with JOINED inheritance**
Add `@Inheritance(strategy = InheritanceType.JOINED)`. Each subtype
gets its own table; the parent table holds the shared columns.

Open `CreditCardPayment.java` and `BankTransferPayment.java`.

**TODO 4: Both subclasses already extend Payment**
Just verify the inheritance compiles and the schema matches the
expectation (one `payment` table plus a child table per subtype).

### Part 3: Money via AttributeConverter

Open `MoneyConverter.java`.

**TODO 5: Implement `convertToDatabaseColumn` and `convertToEntityAttribute`**
The converter packs a `Money(amount, currencyCode)` into a single
`String` column (e.g., "USD:19.99") and unpacks it on read. Use
`@Converter(autoApply = true)` so JPA picks it up for every Money
field automatically.

## Deliverable

`SchemaInspectionTest` passes:
- The customer table has flattened address columns (street, city, etc.)
- The payment table exists with shared columns
- credit_card_payment and bank_transfer_payment exist as JOINED tables
- Money values round-trip correctly through the converter

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

## What's Included

- `Address.java` with TODOs 1
- `Customer.java` with TODO 2
- `Payment.java`, `CreditCardPayment.java`, `BankTransferPayment.java`
  with TODOs 3-4
- `Money.java` and `MoneyConverter.java` with TODO 5
- `Order.java`, complete (uses Money via the converter)
- `SchemaInspectionTest.java`, pre-written
- `application.yml` with SQL logging and `ddl-auto: create-drop`

## Common Mistakes

- **`@Embeddable` without `@Embedded` on the field:** Hibernate may treat the type as a separate entity and fail at startup.
- **JOINED inheritance and missing child tables:** each subtype declares its own table via `@Table(name = ...)`; missing that gives you implicit table names you may not want.
- **AttributeConverter forgetting `@Converter(autoApply = true)`:** then every Money field needs `@Convert(converter = MoneyConverter.class)` explicitly. autoApply removes the boilerplate.
- **JOINED is great for clean schemas, slow for deep hierarchies:** every read JOINs the parent and the child table. Worth knowing for Module 5's discussion.
