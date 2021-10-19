package com.rectangles.rectangleapi.api.validation.validators;

import com.rectangles.rectangleapi.api.validation.constraints.MaxSizeConstraint;
import com.rectangles.rectangleapi.dto.SaveRectangleDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSizeConstraint, List<SaveRectangleDto>> {
    @Override
    public boolean isValid(List<SaveRectangleDto> values, ConstraintValidatorContext context) {
        return values.size() == 2;
    }
}
