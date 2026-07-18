package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;

/**
 * Persists two menu items, each demonstrating the three advanced mapping
 * features: @Embedded Money, @ElementCollection ingredients, @Convert dietary
 * flags. Then reads them back and prints them so viewers can see the results.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");
        try {
            Long noodleId = persistMenuItems(emf);
            readMenuItemBack(emf, noodleId);
        } finally {
            emf.close();
        }
    }

    private static Long persistMenuItems(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        MenuItem noodles = new MenuItem("Spicy Peanut Noodles",
                new Money(new BigDecimal("14.50"), "USD"));
        noodles.addIngredient("rice noodles");
        noodles.addIngredient("peanut sauce");
        noodles.addIngredient("cilantro");
        noodles.addIngredient("lime");
        noodles.addDietaryFlag(DietaryFlag.VEGAN);
        noodles.addDietaryFlag(DietaryFlag.SPICY);

        MenuItem salad = new MenuItem("Garden Salad",
                new Money(new BigDecimal("9.75"), "USD"));
        salad.addIngredient("mixed greens");
        salad.addIngredient("tomato");
        salad.addIngredient("cucumber");
        salad.addDietaryFlag(DietaryFlag.VEGAN);
        salad.addDietaryFlag(DietaryFlag.GLUTEN_FREE);
        salad.addDietaryFlag(DietaryFlag.NUT_FREE);

        em.persist(noodles);
        em.persist(salad);

        em.getTransaction().commit();
        em.close();

        System.out.println("Persisted two menu items. Noodles id: " + noodles.getId());
        return noodles.getId();
    }

    private static void readMenuItemBack(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        MenuItem item = em.find(MenuItem.class, id);
        System.out.println("Loaded: " + item);
        System.out.println("Ingredients: " + item.getIngredients());
        System.out.println("Dietary flags decoded from single string column: "
                + item.getDietaryFlags());

        em.getTransaction().commit();
        em.close();
    }
}
