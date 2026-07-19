package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * A news article. Simple flat entity with an indexed created_at column so
 * keyset pagination has something meaningful to seek on.
 */
@Entity
@Table(name = "article", indexes = {
        @Index(name = "idx_article_created_at", columnList = "created_at")
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 64)
    private String author;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Article() {}

    public Article(String title, String author, LocalDateTime createdAt) {
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("Article[id=%d, %s by %s @ %s]",
                id, title, author, createdAt);
    }
}
