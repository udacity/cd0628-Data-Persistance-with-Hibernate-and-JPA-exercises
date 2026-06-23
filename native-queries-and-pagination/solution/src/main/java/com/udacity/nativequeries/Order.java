package com.udacity.nativequeries;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Maps the three native columns (month, category, total) into a typed
// MonthlyRevenueReport. The query references this by the name attribute.
@SqlResultSetMapping(
    name = "MonthlyRevenueMapping",
    classes = @ConstructorResult(
        targetClass = MonthlyRevenueReport.class,
        columns = {
            @ColumnResult(name = "month",    type = LocalDateTime.class),
            @ColumnResult(name = "category", type = String.class),
            @ColumnResult(name = "total",    type = BigDecimal.class)
        }
    )
)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "placed_at", nullable = false)
    private LocalDateTime placedAt;

    public Order() {
    }

    public Order(String category, BigDecimal amount, LocalDateTime placedAt) {
        this.category = category;
        this.amount = amount;
        this.placedAt = placedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt) { this.placedAt = placedAt; }
}