package com.ncedu.fooddelivery.api.v1.repos.order;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders " +
                   "WHERE date_end IS NULL AND courier_id = :id", nativeQuery = true)
    Order findCouriersActiveOrder(@Param(value = "id") Long id);
}
