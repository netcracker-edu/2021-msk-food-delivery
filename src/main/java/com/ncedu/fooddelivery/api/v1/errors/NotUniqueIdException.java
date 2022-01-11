package com.ncedu.fooddelivery.api.v1.errors;

import lombok.Data;

@Data
public class NotUniqueIdException extends RuntimeException{
    public static String message = "Given IDs aren't unique.";
    public static final String UUID = "aecc5b48-c1ea-4563-aa91-0b214ef92036";
    
    public NotUniqueIdException(){  }
}