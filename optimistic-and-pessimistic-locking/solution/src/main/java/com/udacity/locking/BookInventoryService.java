package com.udacity.locking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookInventoryService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Optimistic update path: read-modify-save with @Retryable to
     * handle conflicts gracefully. Each retry re-reads the latest
     * row before applying its change.
     */
    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 50, multiplier = 2.0))
    public Book updateDescription(long bookId, String newDescription) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book " + bookId + " not found"));
        book.setDescription(newDescription);
        return bookRepository.save(book);
    }

    /**
     * Pessimistic path: acquires a row-level lock via SELECT FOR UPDATE
     * before reading stock. Only one thread executes the body at a
     * time for a given book; the second waits, then sees the updated
     * stock and throws OutOfStockException.
     *
     * @Transactional is required for the lock to scope a transaction.
     */
    @Transactional
    public Book reserveLastCopy(long bookId) {
        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book " + bookId + " not found"));

        if (book.getStock() <= 0) {
            throw new OutOfStockException("Book " + bookId + " is out of stock");
        }

        book.setStock(book.getStock() - 1);
        return bookRepository.save(book);
    }
}