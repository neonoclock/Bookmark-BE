package com.example.ktbapi.post.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.auth.UserPrincipal;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.common.paging.PagedResponse;
import com.example.ktbapi.post.dto.*;
import com.example.ktbapi.post.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://172.16.24.172:5500")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PagedResponse<PostSummaryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "DATE") PostSortKey sort
    ) {
        return ApiResponse.success(service.getPosts(page, limit, sort));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
       
        Long viewerId = principal != null ? principal.getId() : null;

        return ApiResponse.success(service.getPostDetail(postId, viewerId));
    }
    
    @PostMapping
    public ApiResponse<IdResponse> createPost(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody PostCreateRequest req
    ) {
        Long userId = principal.getId();
        return ApiResponse.success(service.createPost(userId, req));
    }
 
    @PatchMapping("/{postId}")
    public ApiResponse<PostUpdatedResponse> updatePost(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest req
    ) {
        Long userId = principal.getId();
        return ApiResponse.success(service.updatePost(userId, postId, req));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long postId
    ) {
        Long userId = principal.getId();
        service.deletePost(userId, postId);
        return ApiResponse.success();
    }
  
    @GetMapping("/nplus-one")
    public ApiResponse<List<PostSummaryResponse>> allWithNPlusOne() {
        return ApiResponse.success(service.getAllPosts_NPlusOne());
    }


    @GetMapping("/entity-graph")
    public ApiResponse<List<PostSummaryResponse>> allWithEntityGraph() {
        return ApiResponse.success(service.getAllPosts_EntityGraph());
    }
}
