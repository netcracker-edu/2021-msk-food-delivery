package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class areCreatedDTO {
    private List<Long> ids;

    public areCreatedDTO(List<Long> ids) {
        this.ids = ids;
    }
}
