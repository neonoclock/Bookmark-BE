package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostUpdateRequest(
        String title,
        String content,
        @JsonProperty("image_url") String imageUrl
) {}
