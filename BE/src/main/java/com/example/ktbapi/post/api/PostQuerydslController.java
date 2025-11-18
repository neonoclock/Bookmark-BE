package com.example.ktbapi.post.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.paging.PagedResponse;
import com.example.ktbapi.post.dto.PostSummaryResponse;
import com.example.ktbapi.post.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/querydsl/posts")
public class PostQuerydslController {

    private final PostService service;

    public PostQuerydslController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PagedResponse<PostSummaryResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer minViews,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "DATE") PostSortKey sort
    ) {
        return ApiResponse.success(
                service.searchPosts(keyword, authorId, minLikes, minViews, page, limit, sort)
        );
    }

    @PostMapping("/views/reset")
    public ApiResponse<Long> resetViews(@RequestParam(defaultValue = "1000") int threshold) {
        return ApiResponse.success(service.resetViews(threshold));
    }
}