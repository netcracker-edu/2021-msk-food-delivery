package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourierRepo extends JpaRepository<Courier, Long> {
    @Query(value = "SELECT COUNT(*) FROM " +
            "couriers INNER JOIN delivery_sessions USING (courier_id) " +
            "WHERE delivery_sessions.end_time IS NULL AND couriers.warehouse_id = :id ",
            nativeQuery = true)
    Short countWorkingCouriersByWarehouse(@Param(value = "id") Long warehouseId);

    @Query(value = "SELECT COUNT(*) FROM " +
            "couriers INNER JOIN delivery_sessions USING (courier_id) " +
                     "INNER JOIN orders USING (courier_id) " +
            "WHERE delivery_sessions.end_time IS NULL AND orders.warehouse_id = :id AND orders.status IN ('COURIER_APPOINTED', 'PACKING', 'DELIVERING')",
            nativeQuery = true)
    Short countDeliveringCouriersByWarehouse(@Param(value = "id") Long warehouseId);
}
