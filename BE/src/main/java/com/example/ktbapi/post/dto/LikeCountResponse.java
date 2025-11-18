package com.example.ktbapi.post.dto;

public class LikeCountResponse {
    private final long count;

    public LikeCountResponse(long count) {
        this.count = count;
    }

    public static LikeCountResponse of(long count) {
        return new LikeCountResponse(count);
    }

    public long getCount() {
        return count;
    }
}
