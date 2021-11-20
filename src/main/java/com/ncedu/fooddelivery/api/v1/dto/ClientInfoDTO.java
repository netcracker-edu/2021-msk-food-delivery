package com.ncedu.fooddelivery.api.v1.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ClientInfoDTO extends UserInfoDTO {

    private String phoneNumber;
    private Double rating;

    public ClientInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
    }

    public ClientInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId, String phoneNumber, Double rating) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
