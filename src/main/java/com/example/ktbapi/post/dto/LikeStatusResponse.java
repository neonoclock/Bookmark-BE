package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeStatusResponse(
        @JsonProperty("post_id") Long postId,
        boolean liked,
        @JsonProperty("like_count") int likeCount
) {}
