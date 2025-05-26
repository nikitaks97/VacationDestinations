package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Destination() {
    }

    public Destination(Long id, String name, String description, String imageUrl, User createdBy, Set<Like> likes, Set<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.likes = likes != null ? likes : new HashSet<>();
        this.comments = comments != null ? comments : new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
