package com.example.ktbapi.common.exception;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(Long id) { super("not_found"); }
    public PostNotFoundException() { super("not_found"); }
}
