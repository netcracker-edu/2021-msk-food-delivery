package com.ncedu.fooddelivery.api.v1.errors;

import lombok.Data;

@Data
public class IncorrectProductPositionWarehouseBindingException extends RuntimeException{
    private String message = "Product position isn't located in needed warehouse. Product position id:";
    public static final String UUID = "f34c91dc-867b-409b-b303-31e8fcbb6307";

    public IncorrectProductPositionWarehouseBindingException(Long id){
        message = String.format("%s {%d}.", message, id);
    }
}