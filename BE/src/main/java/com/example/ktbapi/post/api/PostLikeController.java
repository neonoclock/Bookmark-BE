package com.example.ktbapi.post.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.post.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostLikeController {

    private final PostService postService;

    public PostLikeController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{postId}/like")
    public ApiResponse<Void> like(@PathVariable Long postId,
                                  @RequestParam Long userId) {
        postService.like(userId, postId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{postId}/like")
    public ApiResponse<Void> unlike(@PathVariable Long postId,
                                    @RequestParam Long userId) {
        postService.unlike(userId, postId);
        return ApiResponse.success();
    }
}
