package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductAvailabilityEx extends RuntimeException{
    public static final String msg = "Not enough product(s) at the moment.";
    public static final String uuid = "9f5917ca-0440-4615-a7a5-56d65070e751";

    private Map<Long, Integer> productsAvailableAmount;   // productId -> available amount

    public ProductAvailabilityEx(Map<Long, Integer> productsAvailableAmount) {
        super(msg);
        this.productsAvailableAmount = productsAvailableAmount;
    }
}
