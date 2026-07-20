package com.example.demo;

import jakarta.persistence.*;

import java.util.List;

/**
 * Simulates a race between two "workers" trying to decrement the same
 * inventory row. Runs the same scenario twice:
 *
 *   1. Optimistic locking - both workers load the entity, both mutate it,
 *      the second commit throws OptimisticLockException because its @Version
 *      no longer matches the row.
 *
 *   2. Pessimistic locking - the first worker takes a row lock with
 *      LockModeType.PESSIMISTIC_WRITE (SELECT ... FOR UPDATE in SQL). The
 *      second worker's find(..., PESSIMISTIC_WRITE) blocks until the first
 *      commits. Both operations succeed. No exception.
 *
 * To keep the demo predictable we don't use real threads. We interleave two
 * EntityManagers on the main thread and commit them in a specific order.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");
        try {
            Long id = seed(emf);

            System.out.println("---- 1. Optimistic locking (conflict expected) ----");
            optimisticConflict(emf, id);

            System.out.println("---- 2. Pessimistic locking (both succeed, second waits) ----");
            resetQuantity(emf, id, 100);
            pessimisticSerial(emf, id);

            System.out.println("---- 3. Final state ----");
            printItem(emf, id);
        } finally {
            emf.close();
        }
    }

    private static Long seed(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        InventoryItem item = new InventoryItem("WIDGET-42", 100);
        em.persist(item);
        em.getTransaction().commit();
        em.close();
        return item.getId();
    }

    private static void optimisticConflict(EntityManagerFactory emf, Long id) {
        // Worker A opens a session and loads the row.
        EntityManager a = emf.createEntityManager();
        a.getTransaction().begin();
        InventoryItem itemA = a.find(InventoryItem.class, id);
        System.out.println("  Worker A loaded: " + itemA);

        // Worker B opens its own session and loads the same row.
        EntityManager b = emf.createEntityManager();
        b.getTransaction().begin();
        InventoryItem itemB = b.find(InventoryItem.class, id);
        System.out.println("  Worker B loaded: " + itemB);

        // Both workers try to decrement.
        itemA.setQuantity(itemA.getQuantity() - 10);
        itemB.setQuantity(itemB.getQuantity() - 20);

        // Worker A commits first. Version goes from 0 to 1.
        a.getTransaction().commit();
        a.close();
        System.out.println("  Worker A committed. Row now at version 1.");

        // Worker B tries to commit. Its in-memory version is still 0, so the
        // UPDATE WHERE id=? AND version=0 matches zero rows. Optimistic lock
        // exception fires.
        try {
            b.getTransaction().commit();
            System.out.println("  Worker B committed (unexpected!)");
        } catch (OptimisticLockException | RollbackException e) {
            System.out.println("  Worker B FAILED with " + e.getClass().getSimpleName()
                    + " - the row was updated by someone else.");
        } finally {
            b.close();
        }
    }

    private static void pessimisticSerial(EntityManagerFactory emf, Long id) {
        // Worker A takes a pessimistic write lock. In SQL this is SELECT ... FOR UPDATE.
        EntityManager a = emf.createEntityManager();
        a.getTransaction().begin();
        InventoryItem itemA = a.find(InventoryItem.class, id, LockModeType.PESSIMISTIC_WRITE);
        System.out.println("  Worker A took PESSIMISTIC_WRITE lock on: " + itemA);
        itemA.setQuantity(itemA.getQuantity() - 10);
        a.getTransaction().commit();
        a.close();
        System.out.println("  Worker A committed and released the lock.");

        // Worker B can now take the lock and decrement cleanly.
        EntityManager b = emf.createEntityManager();
        b.getTransaction().begin();
        InventoryItem itemB = b.find(InventoryItem.class, id, LockModeType.PESSIMISTIC_WRITE);
        System.out.println("  Worker B took PESSIMISTIC_WRITE lock on: " + itemB);
        itemB.setQuantity(itemB.getQuantity() - 20);
        b.getTransaction().commit();
        b.close();
        System.out.println("  Worker B committed successfully.");
    }

    private static void resetQuantity(EntityManagerFactory emf, Long id, int newQty) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        InventoryItem item = em.find(InventoryItem.class, id);
        item.setQuantity(newQty);
        em.getTransaction().commit();
        em.close();
    }

    private static void printItem(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        InventoryItem item = em.find(InventoryItem.class, id);
        System.out.println("  " + item);
        em.getTransaction().commit();
        em.close();
    }
}
