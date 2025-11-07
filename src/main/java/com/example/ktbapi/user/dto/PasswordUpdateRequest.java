package com.example.ktbapi.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 변경 요청")
public class PasswordUpdateRequest {
  @NotBlank(message = "current_password is required")
  public String current_password;

  @NotBlank(message = "new_password is required")
  @Size(min = 8, message = "new_password must be >= 8 chars")
  public String new_password;

  @NotBlank(message = "new_password_check is required")
  public String new_password_check;

  @AssertTrue(message = "password_mismatch")
  public boolean isNewPasswordMatch() {
    return new_password != null && new_password.equals(new_password_check);
  }
}
