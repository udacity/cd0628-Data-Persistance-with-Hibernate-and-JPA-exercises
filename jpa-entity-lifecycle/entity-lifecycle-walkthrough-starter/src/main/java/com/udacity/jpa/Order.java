package com.udacity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    // ============================================================
    // TODO 3: Annotate this field with @Id
    //
    // Marks the id field as the primary key.
    //
    //
    // TODO 4: On the same field, add:
    //   @GeneratedValue(strategy = GenerationType.SEQUENCE,
    //                   generator = "order_seq")
    //
    // Tells Hibernate to use a database sequence (rather than an
    // auto-increment column) to assign IDs. The INSERT does NOT
    // fire on persist() the way IDENTITY does. Hibernate reads the
    // next sequence value, assigns it to the in-memory object, and
    // delays the INSERT until flush.
    //
    //
    // TODO 5: On the same field, add:
    //   @SequenceGenerator(name = "order_seq",
    //                      sequenceName = "order_sequence",
    //                      allocationSize = 50)
    //
    // Defines the sequence Hibernate calls. allocationSize = 50
    // tells Hibernate to fetch 50 IDs at a time, so it doesn't have
    // to hit the DB for every single insert. Critical for batch
    // performance.
    // ============================================================
    private Long id;

    private String description;
    private double total;

    public Order() {
    }

    public Order(String description, double total) {
        this.description = description;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}