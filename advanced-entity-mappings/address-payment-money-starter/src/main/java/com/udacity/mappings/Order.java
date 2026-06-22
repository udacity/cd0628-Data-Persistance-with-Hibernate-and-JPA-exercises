package com.udacity.mappings;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    // ============================================================
    // TODO 3: Annotate this field with @Embedded, plus @AttributeOverrides
    // to rename each Address column with a shipping_ prefix:
    //
    //   @Embedded
    //   @AttributeOverrides({
    //       @AttributeOverride(name = "street",
    //                          column = @Column(name = "shipping_street")),
    //       @AttributeOverride(name = "city",
    //                          column = @Column(name = "shipping_city")),
    //       @AttributeOverride(name = "state",
    //                          column = @Column(name = "shipping_state")),
    //       @AttributeOverride(name = "postalCode",
    //                          column = @Column(name = "shipping_postal_code"))
    //   })
    // ============================================================
    private Address shippingAddress;

    // ============================================================
    // TODO 4: Annotate this field with @Embedded, plus @AttributeOverrides
    // to rename each Address column with a billing_ prefix:
    //
    //   @Embedded
    //   @AttributeOverrides({
    //       @AttributeOverride(name = "street",
    //                          column = @Column(name = "billing_street")),
    //       @AttributeOverride(name = "city",
    //                          column = @Column(name = "billing_city")),
    //       @AttributeOverride(name = "state",
    //                          column = @Column(name = "billing_state")),
    //       @AttributeOverride(name = "postalCode",
    //                          column = @Column(name = "billing_postal_code"))
    //   })
    // ============================================================
    private Address billingAddress;

    public Order() {
    }

    public Order(String orderNumber, Address shippingAddress, Address billingAddress) {
        this.orderNumber = orderNumber;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public Address getBillingAddress() { return billingAddress; }
    public void setBillingAddress(Address billingAddress) { this.billingAddress = billingAddress; }
}