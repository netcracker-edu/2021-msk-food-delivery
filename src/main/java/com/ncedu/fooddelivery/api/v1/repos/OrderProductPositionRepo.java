package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Order;
import com.ncedu.fooddelivery.api.v1.entities.OrderProductPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductPositionRepo extends JpaRepository<OrderProductPosition, Long> {
    List<OrderProductPosition> findAllByOrder(Order order);
}
