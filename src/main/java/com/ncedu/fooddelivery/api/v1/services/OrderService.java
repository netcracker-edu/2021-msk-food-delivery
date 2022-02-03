package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.AreCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    Order getOrder(Long id);

    List<OrderInfoDTO> findFiltered(User user, OrderFilterDTO dto, Pageable pageable);

    List<OrderInfoDTO> getOrdersHistory(User user, Long targetId, Pageable pageable);

    List<OrderInfoDTO> getMyOrdersHistory(User user, Pageable pageable);

    CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto);

    Double[] countOrderCost(CountOrderCostRequestDTO.Geo geo, HashMap<Long, Integer> pairs,
                            Long warehouseId);

    AreCreatedDTO createOrder(CreateOrderDTO dto, User user);

    OrderInfoDTO getOrderInfo(Long id, User user);

    void changeOrderStatus(Long id, User user, ChangeOrderStatusDTO dto);

    void replaceCourier(Long orderId, User user);

    void changeDeliveryRating(Long orderId, ChangeRatingDTO dto, User user);

    void changeClientRating(Long orderId, ChangeRatingDTO dto, User user);
}
