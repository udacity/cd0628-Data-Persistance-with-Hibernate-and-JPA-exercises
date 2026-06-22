package com.udacity.relationships;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================================
    // TODO 2: Annotate this field with TWO annotations:
    //   @ManyToOne
    //   @JoinColumn(name = "post_id", nullable = false)
    //
    // Comment is the OWNING side of the BlogPost-Comment relationship.
    // The post_id column carries the foreign key. nullable=false
    // enforces every Comment must belong to a Post.
    // ============================================================
    private BlogPost post;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String body;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    public Comment() {
    }

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.postedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BlogPost getPost() { return post; }
    public void setPost(BlogPost post) { this.post = post; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
}