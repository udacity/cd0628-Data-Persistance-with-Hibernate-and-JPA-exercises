package com.example.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * A product in an e-commerce catalog. Simple flat entity so the demo can focus
 * on query construction rather than relationship traversal.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 32)
    private String category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "in_stock", nullable = false)
    private boolean inStock;

    protected Product() {}

    public Product(String name, String category, BigDecimal price, boolean inStock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.inStock = inStock;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public boolean isInStock() { return inStock; }

    @Override
    public String toString() {
        return String.format("Product[%s, %s, $%s, %s]",
                name, category, price, inStock ? "in stock" : "out of stock");
    }
}
