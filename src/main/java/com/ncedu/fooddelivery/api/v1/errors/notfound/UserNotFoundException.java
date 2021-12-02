package com.ncedu.fooddelivery.api.v1.errors.notfound;

public class UserNotFoundException extends NotFoundEx {

    private final static String UUID = "b912070d-24ce-4a8e-a69d-528f1ce9bcef";

    public UserNotFoundException(String message) {
        super(message, UUID);
    }
}
