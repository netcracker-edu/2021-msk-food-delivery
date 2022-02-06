package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

import lombok.Data;

@Data
public class OrderCostChangedEx extends RuntimeException{
    public static final String msg = "Order cost has changed.";
    public static final String uuid = "4871a0c3-c115-4b50-abe7-f9ffb2662539";

    private Double currentOverallCost, currentDiscount, currentHighDemandCoeff;

    public OrderCostChangedEx(Double currentOverallCost, Double currentDiscount, Double currentHighDemandCoeff) {
        super(msg);
        this.currentOverallCost = currentOverallCost;
        this.currentDiscount = currentDiscount;
        this.currentHighDemandCoeff = currentHighDemandCoeff;
    }
}
