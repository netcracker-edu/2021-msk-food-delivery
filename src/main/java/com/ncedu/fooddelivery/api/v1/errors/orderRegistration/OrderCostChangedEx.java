package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

import java.util.Locale;

public class OrderCostChangedEx extends RuntimeException{
    public static final String msg = "Order cost has changed";
    public static final String uuid = "4871a0c3-c115-4b50-abe7-f9ffb2662539";

    public OrderCostChangedEx(Double currentOverallCost, Double currentDiscount, Double currentHighDemandCoeff) {
         super(String.format(Locale.US, "%s: currentOverallCost=%.2f,currentDiscount=%.2f,currentHighDemandCoeff=%.2f", OrderCostChangedEx.msg, currentOverallCost, currentDiscount, currentHighDemandCoeff));
    }
}
