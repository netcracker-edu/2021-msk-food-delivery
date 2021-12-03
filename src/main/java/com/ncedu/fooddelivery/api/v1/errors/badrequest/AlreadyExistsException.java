package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class AlreadyExistsException extends RuntimeException {
    private static final String uuid = "dcf4565b-4fa8-46c4-984e-22917401d59b";
    private static final String msg = "Entity with such value already exists";

    public AlreadyExistsException(String value) {
        super(String.format("%s: {%s}", msg, value));
    }
    public String getUuid() {
        return uuid;
    }
}
