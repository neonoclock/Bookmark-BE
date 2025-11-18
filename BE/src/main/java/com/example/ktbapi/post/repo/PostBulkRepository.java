package com.example.ktbapi.post.repo;

public interface PostBulkRepository {
    long increaseViews(Long postId);
    long resetViewsOver(int threshold);
    long deleteByAuthorId(Long authorId);
}