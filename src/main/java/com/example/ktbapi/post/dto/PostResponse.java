package com.example.ktbapi.post.dto;

public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        String imageUrl,
        String createdAt,
        int views,
        int likes
) {}
