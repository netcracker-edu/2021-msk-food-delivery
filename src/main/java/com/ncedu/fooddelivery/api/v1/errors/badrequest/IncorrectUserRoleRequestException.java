package com.ncedu.fooddelivery.api.v1.errors.badrequest;

public class IncorrectUserRoleRequestException extends RuntimeException{
    public static final String message = "Requested user has incorrect role for such request";
    public static final String UUID = "76b007f5-6844-428a-a8f9-c8c92c5189cb";

    public IncorrectUserRoleRequestException(){
        super(message);
    }
}
