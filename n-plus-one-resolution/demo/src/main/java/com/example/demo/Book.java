package com.example.demo;

import jakarta.persistence.*;
import java.time.Year;

/**
 * A book by an author. The many side of the OneToMany, owns the relationship
 * via the FK on this table.
 */
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "published_year", nullable = false)
    private Year publishedYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    protected Book() {}

    public Book(String title, Year publishedYear) {
        this.title = title;
        this.publishedYear = publishedYear;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Year getPublishedYear() { return publishedYear; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return String.format("Book[id=%d, %s (%s)]", id, title, publishedYear);
    }
}
