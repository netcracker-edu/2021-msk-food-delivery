package com.ncedu.fooddelivery.api.v1.errors.notfound;

public class OrderNotFoundException extends NotFoundEx{
    private static final String UUID = "436526ef-47bc-49a1-aa45-319a1b46971b";
    private static final String message = "Order with such id not found";

    public OrderNotFoundException(Long id) {
        super(String.format("%s: {%d}", message, id), UUID);
    }

}
