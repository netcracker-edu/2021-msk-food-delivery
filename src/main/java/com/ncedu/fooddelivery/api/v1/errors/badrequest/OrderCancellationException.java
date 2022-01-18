package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class OrderCancellationException extends RuntimeException{
    public static final String msg = "Cannot cancel delivered or cancelled order";
    public static final String uuid = "5cff6cf2-917a-4db7-8678-90bb7d992d18";

    public OrderCancellationException(Long orderId) {
        super(String.format("%s: id=%d", OrderCancellationException.msg, orderId));
    }
}
