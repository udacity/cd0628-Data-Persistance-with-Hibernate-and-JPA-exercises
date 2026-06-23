package com.udacity.locking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int stock;

    // ============================================================
    // TODO 1: Add a @Version field below.
    //
    //   @Version
    //   private Long version;
    //
    // Import:
    //   import jakarta.persistence.Version;
    //
    // Hibernate manages this column automatically: it increments
    // version on every UPDATE and throws OptimisticLockException
    // if the row was modified by another transaction since you
    // read it.
    // ============================================================

    public Book() {
    }

    public Book(String isbn, String title, String author, String description, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.description = description;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}