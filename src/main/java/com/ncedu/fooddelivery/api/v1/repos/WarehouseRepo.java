package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

    @Query(value = "SELECT * FROM warehouses WHERE is_deactivated = false",
           nativeQuery = true)
    List<Warehouse> getActiveWarehouses();
}
