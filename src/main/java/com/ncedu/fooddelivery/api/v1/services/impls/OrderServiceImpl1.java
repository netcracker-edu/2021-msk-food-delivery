package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl1 implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Override
    public Order getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) return null;
        else return optionalOrder.get();
    }


}
