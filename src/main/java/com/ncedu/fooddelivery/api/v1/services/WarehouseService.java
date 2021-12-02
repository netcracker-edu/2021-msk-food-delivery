package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.WarehouseInfoDTO;


public interface WarehouseService {
    WarehouseInfoDTO getWarehouseInfoDTOById(Long id);
}
