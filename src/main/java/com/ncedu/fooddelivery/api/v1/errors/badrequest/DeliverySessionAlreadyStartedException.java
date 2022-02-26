package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class DeliverySessionAlreadyStartedException extends RuntimeException{

    public static final String uuid = "7ae3643d-e71d-4eeb-949d-795c7331b52f";
    public static final String msg = "Delivery session is already started.";
    public DeliverySessionAlreadyStartedException() {
        super(msg);
    }

}
