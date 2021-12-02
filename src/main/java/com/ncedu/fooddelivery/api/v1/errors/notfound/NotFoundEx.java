package com.ncedu.fooddelivery.api.v1.errors.notfound;

import lombok.Data;

@Data
public class NotFoundEx extends RuntimeException{

    private String uuid;

    public NotFoundEx(String message) {
        super(message);
    }

    public NotFoundEx(String message, String uuid) {
        super(message);
       this.uuid = uuid;
    }



}
