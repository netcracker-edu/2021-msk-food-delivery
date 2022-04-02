package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.Data;

@Data
public class OrdersAmountDTO {
    private long amount;

    public OrdersAmountDTO(){}

    public OrdersAmountDTO(long amount) {
        this.amount = amount;
    }
}
