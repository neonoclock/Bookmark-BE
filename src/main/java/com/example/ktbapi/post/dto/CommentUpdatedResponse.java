package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentUpdatedResponse(
        @JsonProperty("comment_id") Long id,
        String content,
        @JsonProperty("updated_at") String updatedAt
) {}