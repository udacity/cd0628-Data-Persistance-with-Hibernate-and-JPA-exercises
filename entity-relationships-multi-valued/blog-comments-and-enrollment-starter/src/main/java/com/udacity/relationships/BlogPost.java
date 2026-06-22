package com.udacity.relationships;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_post")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    // ============================================================
    // TODO 1: Annotate this field with:
    //   @OneToMany(mappedBy = "post",
    //              cascade = CascadeType.ALL,
    //              orphanRemoval = true)
    //
    // mappedBy="post" says Comment owns the foreign key
    // cascade=ALL propagates saves and deletes to comments
    // orphanRemoval=true means removing a Comment from this list
    // triggers a DELETE on that Comment row
    // ============================================================
    private List<Comment> comments = new ArrayList<>();

    public BlogPost() {
    }

    public BlogPost(String title, String body) {
        this.title = title;
        this.body = body;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * Helper method that sets BOTH sides of the relationship at once.
     * Always use this instead of comments.add() directly to avoid
     * inconsistent state.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}