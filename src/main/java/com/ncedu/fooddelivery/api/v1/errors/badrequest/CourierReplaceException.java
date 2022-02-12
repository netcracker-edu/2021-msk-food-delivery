package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class CourierReplaceException extends RuntimeException{
    public static final String msg = "Cannot appoint another courier for delivered or cancelled order.";
    public static final String uuid = "1ef19354-6702-4863-b9cf-5c9e6e2e9c92";

    public CourierReplaceException() {
        super(msg);
    }
}
