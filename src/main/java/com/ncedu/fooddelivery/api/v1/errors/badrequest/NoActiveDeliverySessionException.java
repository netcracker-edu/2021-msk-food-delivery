package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class NoActiveDeliverySessionException extends RuntimeException{
    public static final String uuid = "8524b6d3-44ce-4f3c-bcae-9a3f6b78b9ad";
    public static final String msg = "Currently there is no active delivery session.";
    public NoActiveDeliverySessionException() {
        super(msg);
    }
}
