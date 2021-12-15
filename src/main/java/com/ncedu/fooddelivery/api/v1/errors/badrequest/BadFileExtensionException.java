package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class BadFileExtensionException extends RuntimeException {
    private static final String uuid = "cef35b0e-805b-4012-a64b-27467ef8ac55";
    private static final String msg = "File with such exception not supported!";
    public BadFileExtensionException() {
        super(msg);
    }
    public String getUuid() {
        return uuid;
    }
}
