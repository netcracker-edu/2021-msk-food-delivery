package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.repos.WarehouseRepo;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseServiceImpl1 implements WarehouseService {

    @Autowired
    WarehouseRepo warehouseRepo;

    @Override
    public WarehouseInfoDTO getWarehouseInfoDTOById(Long id) {
        Optional<Warehouse> warehouseOptional = warehouseRepo.findById(id);
        if(warehouseOptional.isPresent()){
            Warehouse warehouse = warehouseOptional.get();
            return new WarehouseInfoDTO(warehouse.getId(), warehouse.getGeo(), warehouse.getDeliveryZone(), warehouse.getAddress(), warehouse.getName(), warehouse.getIsDeactivated());
        }
        return null;
    }
}
