package com.example.ktbapi.post.service;

import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.CommentUpdatedResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getComments(Long postId);

    IdResponse createComment(Long userId, Long postId, CommentCreateOrUpdateRequest req);

    CommentUpdatedResponse updateComment(Long userId, Long postId, Long commentId, CommentCreateOrUpdateRequest req);

    void deleteComment(Long userId, Long postId, Long commentId);
}
