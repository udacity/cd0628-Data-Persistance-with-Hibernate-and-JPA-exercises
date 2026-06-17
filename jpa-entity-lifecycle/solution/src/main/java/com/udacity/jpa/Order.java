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

    // SEQUENCE strategy: Hibernate reads a database sequence to get
    // the next ID, assigns it to the in-memory object, and defers
    // the INSERT until flush. allocationSize=50 lets Hibernate batch
    // inserts efficiently by pre-fetching 50 IDs at a time.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 50)
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