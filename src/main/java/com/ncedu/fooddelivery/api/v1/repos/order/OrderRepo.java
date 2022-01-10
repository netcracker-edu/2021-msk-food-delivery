package com.ncedu.fooddelivery.api.v1.repos.order;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
