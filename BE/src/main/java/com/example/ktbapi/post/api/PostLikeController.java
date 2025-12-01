package com.example.ktbapi.post.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.auth.UserPrincipal;
import com.example.ktbapi.post.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostLikeController {

    private final PostService postService;

    public PostLikeController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{postId}/like")
    public ApiResponse<Void> like(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal.getId();
        postService.like(userId, postId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{postId}/like")
    public ApiResponse<Void> unlike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal.getId();
        postService.unlike(userId, postId);
        return ApiResponse.success();
    }
}
