package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
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

    public UserInfoDTO() {};
}
