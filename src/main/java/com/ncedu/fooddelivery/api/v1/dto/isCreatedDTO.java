package com.ncedu.fooddelivery.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class isCreatedDTO {
    // id > 0 create success
    // id < 0 create fail
    private Long id;
    public isCreatedDTO() {};
}
