package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostUpdatedResponse(
        @JsonProperty("post_id") Long id,
        String title,
        String content,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("updated_at") String updatedAt
) {}
