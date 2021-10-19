package com.rectangles.rectangleapi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_ERROR = "Validation error";

    @ExceptionHandler
    public ResponseEntity<?> resourceNotFoundHandler(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), resourceNotFoundException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
/*
    @ExceptionHandler
    public ResponseEntity<?> generalExceptionHandler(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/
    @ExceptionHandler
    public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException exception) {
        Optional<FieldError> fieldError = Optional.ofNullable(exception.getBindingResult().getFieldError());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), VALIDATION_ERROR,
                fieldError.map(FieldError::getDefaultMessage).orElse(VALIDATION_ERROR));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintExceptionHandler(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage = "";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" ").append(violation.getMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = VALIDATION_ERROR;
        }
        ErrorDetails errorDetails = new ErrorDetails(new Date(), VALIDATION_ERROR, errorMessage);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
