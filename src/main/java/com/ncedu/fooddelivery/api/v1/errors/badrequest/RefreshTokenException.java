package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class RefreshTokenException extends RuntimeException {
    private static final String uuid = "dbf4dce8-d343-4955-a4a0-5c8f600621c5";
    private static final String msg = "User access token can't be updated";
    public RefreshTokenException() {
        super(msg);
    }
    public String getUuid() {
        return uuid;
    }
}
