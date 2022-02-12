package com.ncedu.fooddelivery.api.v1.dto.user;

import com.ncedu.fooddelivery.api.v1.dto.user.UserInfoDTO;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class CourierInfoDTO extends UserInfoDTO {
    private String phoneNumber;
    private Float rating;
    private Long warehouseId;
    private String address;
    private Float currentBalance;

    public CourierInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId, String phoneNumber, Float rating, Long warehouseId, String address, Float currentBalance) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.warehouseId = warehouseId;
        this.address = address;
        this.currentBalance = currentBalance;
    }
}
