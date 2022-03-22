package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.Data;

@Data
public class OrdersAmountDTO {
    private Integer amount;

    public OrdersAmountDTO(){}

    public OrdersAmountDTO(Integer amount) {
        this.amount = amount;
    }
}
