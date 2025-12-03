package com.example.ktbapi.user.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.auth.JwtTokenProvider;
import com.example.ktbapi.common.auth.UserPrincipal;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.user.dto.LoginRequest;
import com.example.ktbapi.user.dto.LoginResponse;
import com.example.ktbapi.user.dto.PasswordUpdateRequest;
import com.example.ktbapi.user.dto.ProfileUpdateRequest;
import com.example.ktbapi.user.dto.SignupRequest;
import com.example.ktbapi.user.dto.UserResponse;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.model.UserRole;
import com.example.ktbapi.user.repo.UserJpaRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserJpaRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private static final int ACCESS_TOKEN_EXPIRES_IN = 60 * 60 * 2;

    public UserController(
            UserJpaRepository userRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ApiResponse<IdResponse> signup(@Valid @RequestBody SignupRequest req) {

        if (userRepo.existsByEmail(req.email)) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }

        UserRole role = req.userRole != null ? req.userRole : UserRole.USER;

        String encodedPassword = passwordEncoder.encode(req.password);

        User user = new User(req.email, encodedPassword, req.nickname, role);

        if (req.profileImage != null && !req.profileImage.isBlank()) {
            user.setProfileImage(req.profileImage);
        }

        userRepo.save(user);
        return ApiResponse.success(new IdResponse(user.getId()));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(req.email, req.password);

        var authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

     
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail()
        );

   
        String refreshToken = null;

        LoginResponse res = new LoginResponse(
                user.getId(),
                "Bearer",
                accessToken,
                refreshToken,
                ACCESS_TOKEN_EXPIRES_IN
        );

        return ApiResponse.success(res);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ApiResponse.success(UserResponse.from(user));
    }

    @PatchMapping("/profile")
    public ApiResponse<Void> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ProfileUpdateRequest req
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.changeNickname(req.nickname);

        if (req.profileImage != null) {
            user.setProfileImage(req.profileImage);
        }

        userRepo.save(user);
        return ApiResponse.success();
    }

    @PatchMapping("/password")
    public ApiResponse<Void> updatePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PasswordUpdateRequest req
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(req.oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.changePassword(passwordEncoder.encode(req.newPassword));
        userRepo.save(user);

        return ApiResponse.success();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepo.delete(user);
        return ApiResponse.success();
    }
}