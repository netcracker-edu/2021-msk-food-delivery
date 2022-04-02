package com.ncedu.fooddelivery.api.v1.dto.deliverySession;

import lombok.Data;

@Data
public class DeliverySessionsAmountDTO {
    private Integer amount;

    public DeliverySessionsAmountDTO(){}

    public DeliverySessionsAmountDTO(Integer amount) {
        this.amount = amount;
    }
}
