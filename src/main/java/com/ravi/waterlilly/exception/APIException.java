package com.ravi.waterlilly.exception;

// Custom exception class for handling API-related errors.
public class APIException extends RuntimeException {
    public APIException() {
    }

    //Constructor with a custom error message.
    public APIException(String message) {
        super(message);
    }
}
