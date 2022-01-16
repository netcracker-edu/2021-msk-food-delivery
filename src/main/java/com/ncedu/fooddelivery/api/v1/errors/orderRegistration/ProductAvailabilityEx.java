package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductAvailabilityEx extends RuntimeException{
    public static final String msg = "Some product(s) aren't available currently.";
    public static String uuid = "9f5917ca-0440-4615-a7a5-56d65070e751";

    private List<Long> notFoundProductsIds;
    private Map<Long, Integer> productsAvailableAmount;   // productId -> available amount

    public ProductAvailabilityEx(List<Long> notFoundProductsIds, Map<Long, Integer> productsAvailableAmount) {
        super(msg);
        this.notFoundProductsIds = notFoundProductsIds;
        this.productsAvailableAmount = productsAvailableAmount;
    }
}
