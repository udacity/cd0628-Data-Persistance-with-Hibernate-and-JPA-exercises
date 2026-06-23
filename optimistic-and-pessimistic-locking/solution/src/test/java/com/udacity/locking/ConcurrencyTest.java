package com.udacity.locking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies both locking strategies on the Book inventory.
 *
 *   - Optimistic conflict: two threads update the same book
 *     simultaneously. The Spring Retry interceptor catches the
 *     OptimisticLockingFailureException, backs off, and the second
 *     update eventually succeeds.
 *
 *   - Pessimistic serialization: two threads call reserveLastCopy on
 *     a book with stock=1. One succeeds and decrements stock to 0;
 *     the other waits, then sees stock=0 and throws OutOfStockException.
 */
@SpringBootTest(classes = Application.class)
class ConcurrencyTest {

    @Autowired
    private BookInventoryService bookInventoryService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void resetSeedBook() {
        // Ensure book 1 has stock=1 for the pessimistic test, no matter
        // which test ran first
        bookRepository.findById(1L).ifPresent(book -> {
            book.setStock(1);
            bookRepository.save(book);
        });
    }

    @Test
    void optimisticConflict_retrySucceedsOnContestedRow() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Book> updateA = () -> bookInventoryService.updateDescription(2L, "Updated by thread A");
        Callable<Book> updateB = () -> bookInventoryService.updateDescription(2L, "Updated by thread B");

        Future<Book> resultA = pool.submit(updateA);
        Future<Book> resultB = pool.submit(updateB);

        Book a = resultA.get();
        Book b = resultB.get();

        pool.shutdown();

        // Both updates eventually succeeded (via Retry); final description
        // is whichever thread won the last write
        assertThat(a).isNotNull();
        assertThat(b).isNotNull();

        Book finalState = bookRepository.findById(2L).orElseThrow();
        assertThat(finalState.getDescription())
                .isIn("Updated by thread A", "Updated by thread B");
    }

    @Test
    void pessimisticSerialization_onlyOneThreadReservesLastCopy() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Object> reserveA = () -> {
            try {
                return bookInventoryService.reserveLastCopy(1L);
            } catch (OutOfStockException e) {
                return e;
            }
        };
        Callable<Object> reserveB = () -> {
            try {
                return bookInventoryService.reserveLastCopy(1L);
            } catch (OutOfStockException e) {
                return e;
            }
        };

        Future<Object> resultA = pool.submit(reserveA);
        Future<Object> resultB = pool.submit(reserveB);

        Object a = resultA.get();
        Object b = resultB.get();

        pool.shutdown();

        // One thread reserved (got back a Book); the other saw stock=0
        // (got back an OutOfStockException)
        long booksReturned = (a instanceof Book ? 1 : 0) + (b instanceof Book ? 1 : 0);
        long outOfStock = (a instanceof OutOfStockException ? 1 : 0) + (b instanceof OutOfStockException ? 1 : 0);

        assertThat(booksReturned).isEqualTo(1);
        assertThat(outOfStock).isEqualTo(1);

        Book finalState = bookRepository.findById(1L).orElseThrow();
        assertThat(finalState.getStock()).isZero();
    }
}