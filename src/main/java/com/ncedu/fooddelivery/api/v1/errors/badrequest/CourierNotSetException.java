package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class CourierNotSetException extends RuntimeException{
    public static final String msg = "Courier is not set currently.";
    public static final String uuid = "4c151408-e652-4497-b911-d630a37e404b";

    public CourierNotSetException() {
        super(msg);
    }
}
