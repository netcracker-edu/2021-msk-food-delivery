package com.ncedu.fooddelivery.api.v1.errors.security;

import lombok.Data;

@Data
public class UserLockedException extends RuntimeException{
    public static final String UUID = "76b007f5-6844-428a-a8f9-c8c92c5189cb";
    public static final String msg = "You're locked.";

    public UserLockedException(){
        super(msg);
    }
}
