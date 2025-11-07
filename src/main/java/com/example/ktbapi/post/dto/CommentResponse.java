package com.example.ktbapi.post.dto;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.post.model.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentResponse(
        @JsonProperty("comment_id") Long id,
        @JsonProperty("author_id") Long authorId,
        @JsonProperty("author_name") String authorName,
        String content,
        @JsonProperty("created_at") String createdAt
) {
    public static CommentResponse from(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getAuthor() != null ? c.getAuthor().getId() : null,
                c.getAuthor() != null ? c.getAuthor().getNickname() : null,
                c.getContent(),
                TimeUtil.format(c.getCreatedAt())
        );
    }
}
