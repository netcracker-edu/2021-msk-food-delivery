package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.vividsolutions.jts.geom.Point;

import java.util.List;


public interface WarehouseService {
    WarehouseInfoDTO getWarehouseInfoDTOById(Long id);
    WarehouseInfoDTO getNearestWarehouse(Point geo);

    List<WarehouseInfoDTO> getActiveWarehouses();
}
