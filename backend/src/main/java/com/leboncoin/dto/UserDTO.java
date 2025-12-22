package com.leboncoin.dto;

import java.time.LocalDateTime;

public class UserDTO {

    private Integer id;
    private String email;
    private Boolean emailVerified;
    private LocalDateTime createdAt;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Integer id, String email, Boolean emailVerified, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
