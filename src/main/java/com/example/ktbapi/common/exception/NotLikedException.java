package com.example.ktbapi.common.exception;

/**
 * 좋아요하지 않은 상태에서 좋아요 취소 시 발생.
 */
public class NotLikedException extends RuntimeException {
    public NotLikedException() {
        super("not_liked");
    }

    public NotLikedException(String message) {
        super(message);
    }
}
