package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ValidationSubError extends ApiSubError{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ValidationSubError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
