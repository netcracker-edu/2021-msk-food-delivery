package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class AreCreatedDTO {
    private List<Long> ids;

    public AreCreatedDTO(List<Long> ids) {
        this.ids = ids;
    }
}
