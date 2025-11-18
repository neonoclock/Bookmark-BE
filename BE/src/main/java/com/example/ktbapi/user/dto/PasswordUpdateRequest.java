package com.example.ktbapi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

@Schema(description = "비밀번호 변경 요청")
public class PasswordUpdateRequest {
  @NotBlank(message = "oldPassword is required")
  public String oldPassword;

  @NotBlank(message = "newPassword is required")
  @Size(min = 8, max = 200, message = "newPassword must be between 8 and 200 chars")
  public String newPassword;

  @NotBlank(message = "newPasswordCheck is required")
  public String newPasswordCheck;

  @AssertTrue(message = "password_mismatch")
  public boolean isNewPasswordMatch() {
    return newPassword != null && newPassword.equals(newPasswordCheck);
  }
}
