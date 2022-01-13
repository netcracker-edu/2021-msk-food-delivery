package com.ncedu.fooddelivery.api.v1.errors.security;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {

    private static final String uuid = "58f522af-51d9-4495-ae31-32fafe3eeaea";
    private static final String msg = "Access denied";

    public CustomAccessDeniedException() {
        super(msg);
    }
    public String getUuid() {
        return uuid;
    }
}
