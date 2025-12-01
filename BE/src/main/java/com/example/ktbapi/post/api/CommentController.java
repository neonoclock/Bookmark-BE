package com.example.ktbapi.post.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.auth.UserPrincipal;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.CommentUpdatedResponse;
import com.example.ktbapi.post.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }


    @GetMapping
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ApiResponse.success(service.getComments(postId));
    }

    @PostMapping
    public ApiResponse<IdResponse> createComment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long postId,
            @RequestBody CommentCreateOrUpdateRequest req
    ) {
        Long userId = principal.getId();
        return ApiResponse.success(service.createComment(userId, postId, req));
    }


    @PatchMapping("/{commentId}")
    public ApiResponse<CommentUpdatedResponse> updateComment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentCreateOrUpdateRequest req
    ) {
        Long userId = principal.getId();
        return ApiResponse.success(service.updateComment(userId, postId, commentId, req));
    }

  
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Long userId = principal.getId();
        service.deleteComment(userId, postId, commentId);
        return ApiResponse.success();
    }
}
