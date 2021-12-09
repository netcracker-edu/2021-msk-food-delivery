package com.ncedu.fooddelivery.api.v1.errors.notfound;

import lombok.Data;

@Data
public class NotFoundEx extends RuntimeException{

    private final static String uuid = "b912070d-24ce-4a8e-a69d-528f1ce9bcef";
    private final static String msg = "Entity with such value not found";

    public NotFoundEx(String value) {
        super(String.format("%s: {%s}", msg, value));
    }

    public String getUuid() {
        return uuid;
    }
}
