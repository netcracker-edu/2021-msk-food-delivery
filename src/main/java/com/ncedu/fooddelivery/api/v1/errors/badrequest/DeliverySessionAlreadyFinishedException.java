package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class DeliverySessionAlreadyFinishedException extends RuntimeException{
    public static final String uuid = "408584b8-48b7-4707-84db-4c45c54015ff";
    public static final String msg = "Delivery session is already finished";
    public DeliverySessionAlreadyFinishedException(Long id) {
        super(String.format("%s: id=%s", msg, id.toString()));
    }
}
