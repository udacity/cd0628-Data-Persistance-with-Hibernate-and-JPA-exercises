package com.udacity.relationships;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    private Long id;

    // ============================================================
    // TODO 2: Annotate this field with three annotations:
    //   @OneToOne
    //   @MapsId
    //   @JoinColumn(name = "id")
    //
    // Makes UserProfile the OWNING side of the relationship.
    // @MapsId tells Hibernate to use the parent User's id as
    // UserProfile's own id (shared primary key). No extra column
    // is needed because the foreign key IS the primary key.
    // ============================================================
    private User user;

    @Column(name = "display_name")
    private String displayName;

    private String bio;

    public UserProfile() {
    }

    public UserProfile(String displayName, String bio) {
        this.displayName = displayName;
        this.bio = bio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}