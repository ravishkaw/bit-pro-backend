package com.ravi.waterlilly.exception;

import com.ravi.waterlilly.payload.APIResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Global exception handler for handling application-wide exceptions.
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Handles validation errors.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            message.append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        });
        APIResponse apiResponse = new APIResponse(message.toString().trim(), false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    //Handles custom API exceptions.
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e) {
        String message = e.getMessage();
        APIResponse response = new APIResponse(message, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handles resource not found exceptions.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    //Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
