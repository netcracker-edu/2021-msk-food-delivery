package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.AreCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    Order getOrder(Long id);

    List<OrderInfoDTO> findFiltered(User user, OrderFilterDTO dto, Pageable pageable);

    List<OrderInfoDTO> getOrdersHistory(User authedUser, User targetUser, Pageable pageable);

    List<OrderInfoDTO> getMyOrdersHistory(User user, Pageable pageable);

    CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto);

    Double[] countOrderCost(CoordsDTO geo, HashMap<Long, Integer> pairs,
                            Long warehouseId);

    AreCreatedDTO createOrder(CreateOrderDTO dto, User user);

    OrderInfoDTO getOrderInfo(Order order, User user);

    void changeOrderStatus(Order order, User user, ChangeOrderStatusDTO dto);

    void replaceCourier(Order order, User user);

    void changeDeliveryRating(Order order, ChangeRatingDTO dto, User user);

    void changeClientRating(Order order, ChangeRatingDTO dto, User user);
}
