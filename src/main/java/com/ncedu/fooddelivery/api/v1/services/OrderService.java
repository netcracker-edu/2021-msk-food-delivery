package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order getOrder(Long id);

    List<OrderInfoDTO> findFiltered(Specification<Order> spec, Pageable pageable);

    List<OrderInfoDTO> getOrdersHistory(User user, Pageable pageable);

    CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto);

    Double[] countOrderCost(CountOrderCostRequestDTO.Geo geo, List<CountOrderCostRequestDTO.ProductAmountPair> pairs,
                            Long warehouseId);

    CreatedOrdersIdDTO createOrder(CreateOrderDTO dto, User user);

    OrderInfoDTO getOrderInfo(Long id, User user);

    void cancelOrder(Long id, User user);

    void changeOrderStatus(Long id, User user);

    void replaceCourier(Long orderId, User user);

    void changeDeliveryRating(Long orderId, BigDecimal newRating, User user);

    void changeClientRating(Long orderId, BigDecimal newRating, User user);
}
