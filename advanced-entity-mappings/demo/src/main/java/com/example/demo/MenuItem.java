package com.example.demo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A restaurant menu item. Shows three advanced mapping features in one place:
 *
 *   1. @Embedded price   - a value type (Money) is embedded, no separate table
 *   2. @ElementCollection - a simple List<String> of ingredients gets its own
 *                           side table with a foreign key back to menu_item
 *   3. @Convert           - a Set<DietaryFlag> is packed into a short String via
 *                           a custom AttributeConverter
 */
@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Value-type embedding. Money's fields become columns on this table.
    @Embedded
    private Money price;

    // Element collection. A List<String> gets its own side table with a FK
    // back to menu_item. Each string is a row.
    @ElementCollection
    @CollectionTable(name = "menu_item_ingredient",
                     joinColumns = @JoinColumn(name = "menu_item_id"))
    @Column(name = "ingredient")
    private List<String> ingredients = new ArrayList<>();

    // Custom conversion. The Set<DietaryFlag> is stored as a compact string
    // (e.g. "VGN") in the dietary_flags column, and converted back on read.
    @Convert(converter = DietaryFlagsConverter.class)
    @Column(name = "dietary_flags", length = 8)
    private Set<DietaryFlag> dietaryFlags = EnumSet.noneOf(DietaryFlag.class);

    protected MenuItem() {}

    public MenuItem(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Money getPrice() { return price; }
    public List<String> getIngredients() { return ingredients; }
    public Set<DietaryFlag> getDietaryFlags() { return dietaryFlags; }

    public void addIngredient(String ingredient) { this.ingredients.add(ingredient); }
    public void addDietaryFlag(DietaryFlag flag) { this.dietaryFlags.add(flag); }

    @Override
    public String toString() {
        return String.format("MenuItem[id=%d, %s, %s, flags=%s]",
                id, name, price, dietaryFlags);
    }
}
