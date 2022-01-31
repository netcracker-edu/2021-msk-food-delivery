package com.ncedu.fooddelivery.api.v1.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreatedOrdersIdDTO {
    private List<Long> ids;

    public CreatedOrdersIdDTO(List<Long> ids) {
        this.ids = ids;
    }
}
