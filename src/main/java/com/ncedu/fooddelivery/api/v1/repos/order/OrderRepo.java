package com.ncedu.fooddelivery.api.v1.repos.order;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders WHERE courier_id = :id",
            countQuery = "SELECT COUNT(*) FROM orders WHERE courier_id = :id",
            nativeQuery = true)
    Page<Order> getCourierOrdersHistory(@Param(value = "id") Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE client_id = :id",
            countQuery = "SELECT COUNT(*) FROM orders WHERE client_id = :id",
            nativeQuery = true)
    Page<Order> getClientOrdersHistory(@Param(value = "id") Long userId, Pageable pageable);

    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    @Query(value = "SELECT * FROM orders " +
                   "WHERE courier_id = :id AND status NOT IN ('CANCELLED', 'DELIVERED')",
            nativeQuery = true)
    Order findCouriersActiveOrder(@Param(value = "id") Long id);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE courier_id = :id OR client_id = :id", nativeQuery = true)
    Integer getOrdersAmount(@Param(value = "id") Long userId);
}
