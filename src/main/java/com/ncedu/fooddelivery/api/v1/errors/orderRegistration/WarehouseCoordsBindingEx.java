package com.ncedu.fooddelivery.api.v1.errors.orderRegistration;

public class WarehouseCoordsBindingEx extends RuntimeException{
    public static final String msg = "Given warehouse id and coordinates do not match each other.";
    public static final String uuid = "c4522dd9-6e17-45a3-8b1e-4d8f5ef826c5";

    public WarehouseCoordsBindingEx() {
        super(msg);
    }
}
