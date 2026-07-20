package com.example.demo;

import jakarta.persistence.*;

/**
 * A stock-keeping unit with a quantity on hand. The @Version field is what
 * makes optimistic locking work. Hibernate reads the version when it loads
 * the entity and includes it in the UPDATE's WHERE clause. If another
 * transaction updated the row in the meantime, the version no longer matches,
 * zero rows update, and Hibernate throws OptimisticLockException.
 */
@Entity
@Table(name = "inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Version
    private long version;

    protected InventoryItem() {}

    public InventoryItem(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public long getVersion() { return version; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("Inventory[id=%d, sku=%s, qty=%d, v=%d]",
                id, sku, quantity, version);
    }
}
