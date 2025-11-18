package com.example.ktbapi.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "게시글 생성 요청")
public class PostCreateRequest {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be <= 200 chars")
    @Schema(description = "제목", example = "첫 번째 글")
    public String title;

    @NotBlank(message = "content is required")
    @Schema(description = "내용", example = "본문 내용입니다")
    public String content;

    @Schema(description = "이미지 URL", example = "https://example.com/image.png")
    @JsonProperty("image_url")
    public String imageUrl;
}
