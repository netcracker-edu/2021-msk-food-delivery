package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.IsCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CountOrderCostRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CountOrderCostResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CreateOrderDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.OrderInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface OrderService {
    Order getOrder(Long id);

    List<OrderInfoDTO> findFiltered(Specification<OrderNotHierarchical> spec, Pageable pageable);

    List<OrderInfoDTO> getOrdersHistory(User user, Pageable pageable);

    CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto);

    Double[] countOrderCost(CountOrderCostRequestDTO.Geo geo, List<CountOrderCostRequestDTO.ProductAmountPair> pairs);

    IsCreatedDTO createOrder(CreateOrderDTO dto, User user);
}
