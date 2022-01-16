package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.Data;

@Data
public class ProductNotEnoughSubError extends ApiSubError{
    private final Long productId;
    private final Integer availableAmount;
    public static final String uuid = "2fe96426-18b5-4116-8e86-d7ed95ab9bdf";
    public static final String msg = "Not enough product in warehouse currently.";

    public ProductNotEnoughSubError(Long productId, Integer availableAmount) {
        this.productId = productId;
        this.availableAmount = availableAmount;
    }
}
