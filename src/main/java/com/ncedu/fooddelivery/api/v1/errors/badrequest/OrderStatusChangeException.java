package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class OrderStatusChangeException extends RuntimeException{
    public static final String msg = "Cannot change status of delivered or cancelled order";
    public static final String uuid = "2b8d66d9-b230-4dce-a7f9-04d21847a67a";

    public OrderStatusChangeException(Long orderId) {
        super(String.format("%s: id=%d", OrderStatusChangeException.msg, orderId));
    }
}
