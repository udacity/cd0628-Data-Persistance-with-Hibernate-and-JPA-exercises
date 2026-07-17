package com.example.demo;

import jakarta.persistence.*;

/**
 * A pet owner. The "one" side of the Owner-Pet ManyToOne relationship.
 * Kept intentionally simple so the demo can focus on relationship mappings.
 */
@Entity
@Table(name = "owner")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column
    private String email;

    protected Owner() {}

    public Owner(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("Owner[id=%d, %s, %s]", id, name, phone);
    }
}
