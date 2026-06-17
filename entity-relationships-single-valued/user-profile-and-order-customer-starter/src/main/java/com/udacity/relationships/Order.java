package com.udacity.relationships;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "placed_at", nullable = false)
    private LocalDateTime placedAt;

    // ============================================================
    // TODO 3: Annotate this field with:
    //   @ManyToOne(fetch = FetchType.LAZY)
    //
    // Many Orders reference one Customer. LAZY means the Customer
    // is NOT loaded when you load the Order. Hibernate proxies it
    // and only hits the DB when you actually call customer.getName().
    // This is what causes the N+1 problem unless you JOIN FETCH.
    //
    //
    // TODO 4: On the same field, add:
    //   @JoinColumn(name = "customer_id", nullable = false)
    //
    // Names the foreign key column explicitly. nullable=false
    // enforces that every Order has a Customer at the DB level.
    // ============================================================
    private Customer customer;

    public Order() {
    }

    public Order(String orderNumber, Customer customer) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.placedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt) { this.placedAt = placedAt; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}