package com.example.ktbapi.post.service;

import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.common.paging.PagedResponse;
import com.example.ktbapi.post.api.PostSortKey;
import com.example.ktbapi.post.dto.*;
import com.example.ktbapi.post.model.Post;

import java.util.List;

public interface PostService {
    PagedResponse<PostSummaryResponse> getPosts(int page, int limit, PostSortKey sort);
    PagedResponse<PostSummaryResponse> searchPosts(String keyword, Long authorId, Integer minLikes, Integer minViews,
                                                   int page, int limit, PostSortKey sort);
    PostDetailResponse getPostDetail(Long postId, Long viewerUserId);
    IdResponse createPost(Long userId, PostCreateRequest req);
    PostUpdatedResponse updatePost(Long userId, Long postId, PostUpdateRequest req);
    void deletePost(Long userId, Long postId);
    void like(Long userId, Long postId);
    void unlike(Long userId, Long postId);

    List<Post> findAllPosts_NPlusOne();
    List<Post> findAllPosts_EntityGraph();

    long resetViews(int threshold);
}
