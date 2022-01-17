package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.vividsolutions.jts.geom.Point;

import java.math.BigDecimal;
import java.util.List;



public interface WarehouseService {
    WarehouseInfoDTO getWarehouseInfoDTOById(Long id);
    WarehouseInfoDTO getNearestWarehouse(Point geo);
    WarehouseInfoDTO getNearestWarehouse(BigDecimal lat, BigDecimal lon);
    List<WarehouseInfoDTO> getActiveWarehouses();
    Warehouse findById(Long id);
}
