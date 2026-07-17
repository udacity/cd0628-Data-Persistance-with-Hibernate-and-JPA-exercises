package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

/**
 * Walks through the JPA entity lifecycle by moving a Pilot through each state:
 *
 *   TRANSIENT  -> MANAGED   (persist)
 *   MANAGED    -> DETACHED  (transaction close / clear / detach)
 *   DETACHED   -> MANAGED   (merge)
 *   MANAGED    -> REMOVED   (remove)
 *
 * The demo intentionally uses several small transactions instead of one big one so
 * each transition is easy to point at while narrating.
 */
public class DemoRunner {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");

        try {
            Long pilotId = createAndPersist(emf);         // TRANSIENT -> MANAGED
            updateWhileManaged(emf, pilotId);             // MANAGED   (dirty checking)
            Pilot detached = loadThenDetach(emf, pilotId); // MANAGED  -> DETACHED
            mergeDetached(emf, detached);                 // DETACHED  -> MANAGED
            removePilot(emf, pilotId);                    // MANAGED  -> REMOVED
        } finally {
            emf.close();
        }
    }

    /** Create a Pilot in memory (TRANSIENT), then persist to make it MANAGED. */
    private static Long createAndPersist(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pilot pilot = new Pilot("Amelia Reyes", "ATP-42911",
                LocalDate.of(2019, 4, 12), 4200, true);
        System.out.println("Before persist (TRANSIENT): id=" + pilot.getId());

        em.persist(pilot);
        System.out.println("After persist (MANAGED):    id=" + pilot.getId());

        em.getTransaction().commit();
        em.close();
        return pilot.getId();
    }

    /** Load a MANAGED pilot, mutate a field, commit. No explicit UPDATE call is needed. */
    private static void updateWhileManaged(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pilot pilot = em.find(Pilot.class, id);
        pilot.setFlightHours(pilot.getFlightHours() + 50);

        em.getTransaction().commit();
        em.close();

        System.out.println("Dirty checking flushed the update automatically.");
    }

    /** Load into MANAGED state, then transition to DETACHED. */
    private static Pilot loadThenDetach(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pilot pilot = em.find(Pilot.class, id);
        em.detach(pilot);
        System.out.println("Pilot is now DETACHED - Hibernate no longer tracks changes.");

        em.getTransaction().commit();
        em.close();
        return pilot;
    }

    /** Mutate the detached pilot offline, then reattach with merge. */
    private static void mergeDetached(EntityManagerFactory emf, Pilot detached) {
        detached.setActive(false); // this change is NOT tracked

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pilot managed = em.merge(detached);
        System.out.println("After merge (MANAGED again): " + managed);

        em.getTransaction().commit();
        em.close();
    }

    /** MANAGED -> REMOVED. Actual DELETE fires at commit. */
    private static void removePilot(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Pilot pilot = em.find(Pilot.class, id);
        em.remove(pilot);
        System.out.println("Pilot marked REMOVED - DELETE fires at commit.");

        em.getTransaction().commit();
        em.close();
    }
}
