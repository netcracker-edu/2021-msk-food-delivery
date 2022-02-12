package com.ncedu.fooddelivery.api.v1.repos.orderProductPosition;

import com.ncedu.fooddelivery.api.v1.entities.orderProductPosition.OrderNotHierarchicalProductPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderNotHierarchicalProductPositionRepo extends JpaRepository<OrderNotHierarchicalProductPosition, Long> {
    @Query(value = "SELECT * FROM orders_product_positions WHERE order_id = :id", nativeQuery = true)
    List<OrderNotHierarchicalProductPosition> findAllByOrderId(@Param(value = "id") Long orderId);
}
