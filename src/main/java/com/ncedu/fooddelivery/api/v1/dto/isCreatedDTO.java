package com.ncedu.fooddelivery.api.v1.dto;

import lombok.Data;

@Data
public class isCreatedDTO {
    // id > 0 create success
    // id < 0 create fail
    private Long id;

    public isCreatedDTO(Long id) {
        this.id = id;
    }

    public isCreatedDTO() {
    }
}
