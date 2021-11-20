package com.ncedu.fooddelivery.api.v1.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ModeratorInfoDTO extends UserInfoDTO {

    private Long warehouseId;

    public ModeratorInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
    }

    public ModeratorInfoDTO(Long id, String role, String fullName, String email, Timestamp lastSigninDate, UUID avatarId, Long warehouseId) {
        super(id, role, fullName, email, lastSigninDate, avatarId);
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
