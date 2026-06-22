package com.udacity.mappings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

// ============================================================
// TODO 5: Annotate this class with:
//   @Inheritance(strategy = InheritanceType.JOINED)
//
// Tells Hibernate each subclass gets its own table; the parent
// payment table holds shared columns and the FK link the subclass
// tables join back to.
// ============================================================
@Entity
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Money amount;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    public Payment() {
    }

    public Payment(Money amount) {
        this.amount = amount;
        this.processedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}