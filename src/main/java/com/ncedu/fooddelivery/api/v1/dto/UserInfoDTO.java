package com.ncedu.fooddelivery.api.v1.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class UserInfoDTO {
    private Long id;
    private String role;
    private String fullName;
    private String email;
    private Timestamp lastSigninDate;
    private UUID avatarId;

    public UserInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId) {
        this.id = id;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.lastSigninDate = lastSigninDate;
        this.avatarId = avatarId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastSigninDate() {
        return lastSigninDate;
    }

    public void setLastSigninDate(Timestamp lastSigninDate) {
        this.lastSigninDate = lastSigninDate;
    }

    public UUID getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(UUID avatarId) {
        this.avatarId = avatarId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
