package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.order.Order;

public interface OrderService {
    Order getOrder(Long id);
}
