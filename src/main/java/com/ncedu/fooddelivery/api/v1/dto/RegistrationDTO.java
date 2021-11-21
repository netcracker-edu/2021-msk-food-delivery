package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RegistrationDTO {

    //common user info
    private String role;
    private String password;
    private String fullName;
    private String email;
    private UUID avatarId;

    //client additional fields
    private String paymentData;
    private String phoneNumber;
    private Double rating;

    //moderator additional fields
    private Long warehouseId;
}
