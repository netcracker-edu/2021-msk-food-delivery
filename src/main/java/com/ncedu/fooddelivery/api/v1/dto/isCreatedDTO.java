package com.ncedu.fooddelivery.api.v1.dto;

public class isCreatedDTO {
    // id > 0 create success
    // id < 0 create fail
    private Long id;

    public isCreatedDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
