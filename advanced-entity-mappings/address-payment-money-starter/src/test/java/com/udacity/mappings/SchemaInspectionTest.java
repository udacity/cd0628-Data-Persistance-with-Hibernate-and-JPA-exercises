package com.udacity.mappings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that Hibernate generated the expected schema from the entity
 * mappings:
 *   - Customer has an inline address (street, city, state, postal_code)
 *   - Order has eight address columns (shipping_* and billing_*)
 *   - Payment uses JOINED inheritance with separate subclass tables
 *   - Money converter round-trips through a BigDecimal column
 *
 * Hibernate creates the schema on startup (ddl-auto: create), so this
 * test inspects what came out.
 */
@SpringBootTest(classes = Application.class)
@Transactional
class SchemaInspectionTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void customer_hasInlineAddressColumns() {
        List<String> columns = columnsOf("customer");
        assertThat(columns).contains("street", "city", "state", "postal_code");
    }

    @Test
    void orders_hasShippingAndBillingAddressColumns() {
        List<String> columns = columnsOf("orders");
        assertThat(columns).contains(
                "shipping_street", "shipping_city", "shipping_state", "shipping_postal_code",
                "billing_street", "billing_city", "billing_state", "billing_postal_code"
        );
    }

    @Test
    void payment_usesJoinedInheritanceWithSubclassTables() {
        List<String> tables = jdbc.queryForList(
                "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = 'public' AND table_type = 'BASE TABLE'",
                String.class);

        assertThat(tables).contains("payment", "credit_card_payment", "bank_transfer_payment");
    }

    @Test
    void moneyConverter_roundTripsViaBigDecimalColumn() {
        Money amount = new Money(new BigDecimal("199.99"));
        CreditCardPayment payment = new CreditCardPayment(amount, "1234", "VISA");
        em.persist(payment);
        em.flush();
        em.clear();

        CreditCardPayment loaded = em.find(CreditCardPayment.class, payment.getId());
        assertThat(loaded.getAmount()).isNotNull();
        assertThat(loaded.getAmount().getAmount()).isEqualByComparingTo("199.99");
        assertThat(loaded.getAmount().getCurrency()).isEqualTo("USD");
    }

    private List<String> columnsOf(String tableName) {
        return jdbc.queryForList(
                "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_schema = 'public' AND table_name = ?",
                String.class,
                tableName);
    }
}