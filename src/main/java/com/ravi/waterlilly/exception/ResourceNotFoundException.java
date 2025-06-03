package com.ravi.waterlilly.exception;

// Custom exception for handling resource not found errors.
public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldValue;
    Long fieldId;

    public ResourceNotFoundException() {
    }

    //Constructor for exceptions where the field value is a string.
    public ResourceNotFoundException(String resourceName, String field, String fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, field, fieldValue));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    //Constructor for exceptions where the field value is a number
    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s : %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
