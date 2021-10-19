package com.rectangles.rectangleapi.api.validation.constraints;

import com.rectangles.rectangleapi.api.validation.validators.MaxSizeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MaxSizeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSizeConstraint {
    String message() default "The input list has to have exactly 2 elements.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
