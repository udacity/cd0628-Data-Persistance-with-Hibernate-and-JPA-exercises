package com.udacity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {

    // ============================================================
    // TODO 1: Annotate this field with @Id
    //
    // Marks the id field as the primary key.
    //
    //
    // TODO 2: On the same field, add:
    //   @GeneratedValue(strategy = GenerationType.IDENTITY)
    //
    // Tells Hibernate to let the database assign the ID via an
    // auto-increment column. The INSERT will fire immediately on
    // persist() because Hibernate needs the row to learn the ID.
    // ============================================================
    private Long id;

    private String name;
    private String email;

    public Customer() {
    }

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}