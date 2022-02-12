package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.Data;

@Data
public class ProductNotEnoughSubError extends ApiSubError{
    private final Long productId;
    private final Integer availableAmount;

    public ProductNotEnoughSubError(Long productId, Integer availableAmount) {
        this.productId = productId;
        this.availableAmount = availableAmount;
    }
}
