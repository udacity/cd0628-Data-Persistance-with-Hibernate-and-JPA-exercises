# Exercise: Embeddable Address, Inheritance, and a Money Converter

## Overview

Three advanced mapping patterns applied to one e-commerce starter.
You'll extract Address as an `@Embeddable` reused by Customer (one
address) and Order (two addresses: shipping and billing), configure
Payment with JOINED inheritance and two concrete subtypes, and write
an `AttributeConverter` so Money is stored as a BigDecimal. A
pre-written SchemaInspectionTest verifies the DDL Hibernate generates.

## Exercise Instructions

Open the starter project and work through the TODOs across six files.
Hibernate generates the schema (`ddl-auto: create`), so the test
inspects what came out.

### Part 1: Embeddable Address

Open `Address.java`.

**TODO 1: Annotate the class with `@Embeddable`**
Tells Hibernate this is a value type, not an entity. No separate
address table will be created.

Open `Customer.java`.

**TODO 2: Annotate the `address` field with `@Embedded`**
Embeds the Address columns into the customer table using default
column names (`street`, `city`, `state`, `postal_code`).

Open `Order.java`. Order has TWO Address fields: `shippingAddress`
and `billingAddress`. Both embed into the orders table, so the column
names MUST be distinct or Hibernate will fail to start.

**TODO 3: Annotate the `shippingAddress` field with `@Embedded`, plus `@AttributeOverrides`**
Rename each column with a `shipping_` prefix:
- `street` → `shipping_street`
- `city` → `shipping_city`
- `state` → `shipping_state`
- `postalCode` → `shipping_postal_code`

**TODO 4: Annotate the `billingAddress` field with `@Embedded`, plus `@AttributeOverrides`**
Rename each column with a `billing_` prefix:
- `street` → `billing_street`
- `city` → `billing_city`
- `state` → `billing_state`
- `postalCode` → `billing_postal_code`

### Part 2: JOINED Inheritance

Open `Payment.java`.

**TODO 5: Annotate the class with `@Inheritance(strategy = InheritanceType.JOINED)`**
Each subclass gets its own table; the parent holds shared columns and
the FK link.

Open `CreditCardPayment.java`.

**TODO 6: Annotate the class with `@Entity`**
Marks it as a persistent subtype. No `@Inheritance` here — that lives
on the parent.

Open `BankTransferPayment.java`.

**TODO 7: Annotate the class with `@Entity`**
Same as the credit card subtype.

### Part 3: Money AttributeConverter

Open `MoneyConverter.java`.

**TODO 8: Annotate the class with `@Converter(autoApply = true)`**
`autoApply = true` means Hibernate uses this converter for any field
of type Money without you having to declare it on each field.

**TODO 9: Implement `convertToDatabaseColumn(Money money)`**
Return the BigDecimal `amount` from the Money object. Handle null
gracefully — return null if the input is null.

**TODO 10: Implement `convertToEntityAttribute(BigDecimal value)`**
Build and return a Money object from the BigDecimal. Default the
currency to "USD" if you need to. Handle null gracefully.

## Deliverable

`SchemaInspectionTest` passes. The generated DDL shows:
- Address columns appear inline on the `customer` table (street, city,
  state, postal_code)
- Order has eight address columns: `shipping_street`, `shipping_city`,
  `shipping_state`, `shipping_postal_code`, plus the four `billing_`
  equivalents
- A `payment` table plus separate `credit_card_payment` and
  `bank_transfer_payment` tables, joined on `payment.id`
- Money fields stored as a BigDecimal column wherever Money is used

## What's Included

- `Address.java` with TODO 1
- `Customer.java` with TODO 2
- `Order.java` with TODOs 3-4
- `Payment.java` with TODO 5
- `CreditCardPayment.java` with TODO 6
- `BankTransferPayment.java` with TODO 7
- `MoneyConverter.java` with TODOs 8-10
- `Money.java`, complete value type
- `SchemaInspectionTest.java`, pre-written
- `application.yml` with `ddl-auto: create` and DDL logging on

## Common Mistakes

- **Two `@Embedded` fields of the same type without `@AttributeOverrides`:** Hibernate fails to start because both try to define the same column names in the same table
- **`@Inheritance` on a subclass instead of the parent:** the strategy belongs on the root class
- **Choosing `SINGLE_TABLE` when subtypes have many distinct columns:** leaves a wide table full of nulls; JOINED keeps things tidy
- **`AttributeConverter` that doesn't handle null:** NPE the first time a nullable column is read
- **Forgetting `autoApply = true`:** the converter is silently ignored unless you annotate every Money field with `@Convert`