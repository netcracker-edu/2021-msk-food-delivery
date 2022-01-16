package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.Data;

@Data
public class ProductNotFoundSubError extends ApiSubError{
    private final Long productId;
    public static final String uuid = "2fe96426-18b5-4116-8e86-d7ed95ab9bdf";
    public static final String msg = "Product not found.";

    public ProductNotFoundSubError(Long productId) {
        this.productId = productId;
    }
}
