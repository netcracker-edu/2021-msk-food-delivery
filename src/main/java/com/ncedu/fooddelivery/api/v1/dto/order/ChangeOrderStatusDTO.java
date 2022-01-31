package com.ncedu.fooddelivery.api.v1.dto.order;

import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderStatusDTO {
    @NotNull
    private OrderStatus newStatus;

    public ChangeOrderStatusDTO(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public ChangeOrderStatusDTO() {
    }
}
