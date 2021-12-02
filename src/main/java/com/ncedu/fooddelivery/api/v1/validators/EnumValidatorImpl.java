package com.ncedu.fooddelivery.api.v1.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValid, String> {

    List<String> enumValues = new ArrayList<String>();

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        Enum[] enumValArr = enumClass.getEnumConstants();

        for (Enum enumVal : enumValArr) {
            enumValues.add(enumVal.toString().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return enumValues.contains(value.toUpperCase());
    }
}
