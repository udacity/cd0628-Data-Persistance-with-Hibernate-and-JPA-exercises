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

    // UserProfile is the owning side. @MapsId uses the parent User's
    // id as this entity's id (shared primary key).
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
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