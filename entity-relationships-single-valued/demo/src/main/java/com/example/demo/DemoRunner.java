package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

/**
 * Walks through creating an Owner, two Pets belonging to that Owner (ManyToOne),
 * each with its own MedicalRecord (OneToOne), then loads a Pet and traverses
 * both relationships.
 *
 * Watch the SQL: cascade saves the MedicalRecord along with the Pet, and the
 * JoinColumn on Pet gives us the foreign keys for both associations.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");

        try {
            Long petId = createOwnerWithPets(emf);
            traversePetRelationships(emf, petId);
        } finally {
            emf.close();
        }
    }

    /**
     * Persist an Owner and two Pets (each with a MedicalRecord). One Owner
     * INSERT, two Pet INSERTs, two MedicalRecord INSERTs (cascaded from Pet).
     */
    private static Long createOwnerWithPets(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Owner owner = new Owner("Priya Alvarez", "555-0142", "priya@example.com");
        em.persist(owner);

        MedicalRecord bellaRecord = new MedicalRecord(
                "none", "up to date", LocalDate.of(2026, 5, 10));
        MedicalRecord miloRecord = new MedicalRecord(
                "chicken", "due for booster", LocalDate.of(2026, 2, 3));

        Pet bella = new Pet("Bella", "Golden Retriever",
                LocalDate.of(2020, 6, 14), owner, bellaRecord);
        Pet milo = new Pet("Milo", "Tabby Cat",
                LocalDate.of(2022, 3, 21), owner, miloRecord);

        em.persist(bella);
        em.persist(milo);

        em.getTransaction().commit();
        em.close();

        System.out.println("Persisted owner and two pets. Bella's id: " + bella.getId());
        return bella.getId();
    }

    /**
     * Load a Pet by id, then traverse both associations to trigger the lazy
     * fetches. Watch for the extra SELECTs on Owner and MedicalRecord.
     */
    private static void traversePetRelationships(EntityManagerFactory emf, Long petId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pet pet = em.find(Pet.class, petId);
        System.out.println("Loaded: " + pet);

        // Each of these triggers a lazy fetch and emits a SELECT.
        System.out.println("Owner:  " + pet.getOwner());
        System.out.println("Record: " + pet.getMedicalRecord());

        em.getTransaction().commit();
        em.close();
    }
}
