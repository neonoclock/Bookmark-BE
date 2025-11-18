package com.example.ktbapi.common.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
    public InvalidRequestException() {
        super("invalid_request");
    }
}
