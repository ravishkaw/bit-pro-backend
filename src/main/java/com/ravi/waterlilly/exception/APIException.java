package com.ravi.waterlilly.exception;

public class APIException extends RuntimeException {
    public APIException(){

    }

    public APIException(String message){
        super(message);
    }
}
