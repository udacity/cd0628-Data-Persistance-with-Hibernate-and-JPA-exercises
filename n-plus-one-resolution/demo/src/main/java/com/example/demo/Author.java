package com.example.demo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An author with a lazy collection of books. The parent side of the
 * OneToMany, kept intentionally lazy so we can trigger the N+1 problem
 * on purpose in the demo.
 */
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Lazy by default, which is what makes N+1 easy to reproduce.
    // We load a list of authors, then iterate and touch each author's
    // books, and each touch fires its own SELECT.
    @OneToMany(mappedBy = "author",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    protected Author() {}

    public Author(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBooks() { return books; }

    public void addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
    }

    @Override
    public String toString() {
        return String.format("Author[id=%d, %s]", id, name);
    }
}
