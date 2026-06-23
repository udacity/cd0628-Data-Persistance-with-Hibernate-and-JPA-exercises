package com.udacity.locking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class BookInventoryService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Optimistic update path: simple read-modify-save with @Retryable
     * to handle conflicts gracefully. Each retry re-reads the latest
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

    // ============================================================
    // TODO 3: Annotate this method with @Transactional.
    //
    // Import:
    //   import org.springframework.transaction.annotation.Transactional;
    //
    // The pessimistic lock needs a transaction to scope its duration.
    // Without @Transactional you'll get "No active transaction".
    //
    //
    // TODO 4: Implement the method body in order:
    //   1. Call bookRepository.findByIdForUpdate(bookId) -- this
    //      acquires the row-level lock
    //   2. If Optional is empty, throw BookNotFoundException
    //   3. If book.getStock() > 0, decrement stock and save
    //   4. If book.getStock() <= 0, throw OutOfStockException
    //
    // Because of the pessimistic lock, only one thread enters this
    // method body at a time for a given book. The second thread
    // waits, then sees the updated stock value after the first commits.
    // ============================================================
    public Book reserveLastCopy(long bookId) {
        return null; // placeholder
    }
}