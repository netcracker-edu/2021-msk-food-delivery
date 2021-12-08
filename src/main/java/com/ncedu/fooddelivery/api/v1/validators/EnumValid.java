package com.ncedu.fooddelivery.api.v1.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value can't be null")
@ReportAsSingleViolation
public @interface EnumValid {

    Class<? extends Enum<?>> enumClazz();
    String message() default "Value is not valid";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
