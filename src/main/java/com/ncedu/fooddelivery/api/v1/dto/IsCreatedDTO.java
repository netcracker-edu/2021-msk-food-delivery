package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

@Data
public class IsCreatedDTO {
    // id > 0 create success
    // id < 0 create fail
    private Long id;

    public IsCreatedDTO(Long id) {
        this.id = id;
    }

    public IsCreatedDTO() {
    }
}
