package com.example.ktbapi.common.exception;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException() {
        super("already_liked");
    }
}
