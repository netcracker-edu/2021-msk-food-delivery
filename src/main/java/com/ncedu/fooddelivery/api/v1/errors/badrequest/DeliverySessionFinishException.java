package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class DeliverySessionFinishException extends RuntimeException{
    public static final String uuid = "de582bca-530d-4ffb-a4e4-72df9ac6c1e8";
    public static final String msg = "Courier is already appointed to order: id=";
    public DeliverySessionFinishException(Long activeOrderId) {
        super(String.format("%s%s", msg, activeOrderId.toString()));
    }
}
