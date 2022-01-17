package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

public class CourierAvailabilityEx extends RuntimeException{
    public static final String msg = "No available couriers at the moment.";
    public static final String uuid = "6979bd9b-3a64-46c6-88d5-79917ed78616";

    public CourierAvailabilityEx() {
        super(msg);
    }
}
