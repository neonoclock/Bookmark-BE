package com.example.ktbapi.common.exception;

/**
 * 댓글을 찾을 수 없을 때 발생하는 예외.
 */
public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super("comment_not_found (id=" + id + ")");
    }

    public CommentNotFoundException() {
        super("comment_not_found");
    }
}
