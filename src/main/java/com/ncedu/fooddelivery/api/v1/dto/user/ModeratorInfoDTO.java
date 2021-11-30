package com.ncedu.fooddelivery.api.v1.dto.user;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ModeratorInfoDTO extends UserInfoDTO {

    private Long warehouseId;

    public ModeratorInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId, Long warehouseId) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
        this.warehouseId = warehouseId;
    }
}
