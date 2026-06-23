package com.udacity.locking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // ============================================================
    // TODO 2: Add a findByIdForUpdate method with a pessimistic lock.
    //
    //   @Lock(LockModeType.PESSIMISTIC_WRITE)
    //   @Query("SELECT b FROM Book b WHERE b.id = :id")
    //   Optional<Book> findByIdForUpdate(@Param("id") Long id);
    //
    // Required imports:
    //   import jakarta.persistence.LockModeType;
    //   import org.springframework.data.jpa.repository.Lock;
    //   import org.springframework.data.jpa.repository.Query;
    //   import org.springframework.data.repository.query.Param;
    //
    // Spring Data picks up the @Lock and translates to
    // SELECT ... FOR UPDATE (PostgreSQL emits FOR NO KEY UPDATE).
    // Any other transaction trying to read or write the same row
    // blocks until this one commits.
    // ============================================================
}