package com.example.ktbapi.post.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 생성/수정 요청")
public class CommentCreateOrUpdateRequest {
    @NotBlank(message = "content is required")
    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다!")
    public String content;
}
