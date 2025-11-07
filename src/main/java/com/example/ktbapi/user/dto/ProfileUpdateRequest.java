package com.example.ktbapi.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 수정 요청")
public class ProfileUpdateRequest {
  @NotBlank(message = "nickname is required")
  @Size(max = 30, message = "nickname must be <= 30 chars")
  public String nickname;

  public String profile_image;
}
