package com.ncedu.fooddelivery.api.v1.errors;

import lombok.Data;

@Data
public class ProductPositionNotEnoughException extends RuntimeException{


    private String message = "Current amount of product position isn't enough. Product position id: ";
    public static final String UUID = "1ad62d54-8385-4316-848b-e5ed91b998e5";

    public ProductPositionNotEnoughException(Long id){
        message = String.format("%s {%d}.", message, id);
    }

}