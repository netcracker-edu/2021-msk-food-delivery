package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ClientInfoDTO extends UserInfoDTO {

    private String phoneNumber;
    private Double rating;

    public ClientInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId, String phoneNumber, Double rating) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }
}
