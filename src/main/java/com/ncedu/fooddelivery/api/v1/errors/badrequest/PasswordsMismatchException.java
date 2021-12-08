package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class PasswordsMismatchException extends RuntimeException {
    private static final String uuid = "630f31e6-225b-4e6f-9860-6b45e55b2743";
    private static final String msg = "Passwords mismatch";
    public PasswordsMismatchException() {
        super(msg);
    }
    public String getUuid() {
        return uuid;
    }
}
