package com.example.ktbapi.common.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("unauthorized");
    }
}
