package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.OrderInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderNotHierarchicalRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl1 implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderNotHierarchicalRepo orderNotHierarchicalRepo;

    @Override
    public Order getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) return null;
        else return optionalOrder.get();
    }

    @Override
    public List<OrderInfoDTO> findFiltered(Specification<OrderNotHierarchical> spec, Pageable pageable) {
        return orderNotHierarchicalRepo.findAll(spec, pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public Order findCouriersActiveOrder(Courier courier) {
        return orderRepo.findCouriersActiveOrder(courier.getId());
    }

    public OrderInfoDTO convertToOrderInfoDTO(OrderNotHierarchical orderNotHierarchical){
        Order order = orderRepo.findById(orderNotHierarchical.getId()).get();
        return new OrderInfoDTO(
                order.getId(), order.getClient(), order.getAddress(),
                order.getCoordinates(), order.getWarehouse(),
                order.getCourier(), order.getStatus(), order.getDateStart(),
                order.getDateEnd(), order.getOverallCost(), order.getHighDemandCoeff(),
                order.getDiscount(), order.getPromoCodeId(), order.getClientRating(),
                order.getDeliveryRating()
        );
    }

    public OrderInfoDTO convertToOrderInfoDTO(Order order){
        return new OrderInfoDTO(
                order.getId(), order.getClient(), order.getAddress(),
                order.getCoordinates(), order.getWarehouse(),
                order.getCourier(), order.getStatus(), order.getDateStart(),
                order.getDateEnd(), order.getOverallCost(), order.getHighDemandCoeff(),
                order.getDiscount(), order.getPromoCodeId(), order.getClientRating(),
                order.getDeliveryRating()
        );
    }
}
