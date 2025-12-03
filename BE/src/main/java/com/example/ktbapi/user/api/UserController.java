package com.example.ktbapi.user.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.auth.JwtTokenProvider;
import com.example.ktbapi.common.auth.RefreshToken;
import com.example.ktbapi.common.auth.RefreshTokenRepository;
import com.example.ktbapi.common.auth.UserPrincipal;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.user.dto.*;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.model.UserRole;
import com.example.ktbapi.user.repo.UserJpaRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserJpaRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int ACCESS_TOKEN_EXPIRES_IN = 60 * 60 * 2;

    public UserController(
            UserJpaRepository userRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
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

    @Transactional
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(req.email, req.password);

        var authentication = authenticationManager.authenticate(token);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail()
        );
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(
                user.getId(),
                user.getEmail()
        );

        refreshTokenRepository.deleteByUserId(user.getId());
        RefreshToken refreshToken = RefreshToken.of(
                user.getId(),
                refreshTokenStr,
                LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValiditySeconds())
        );
        refreshTokenRepository.save(refreshToken);

        LoginResponse res = new LoginResponse(
                user.getId(),
                "Bearer",
                accessToken,
                refreshTokenStr,
                ACCESS_TOKEN_EXPIRES_IN
        );

        return ApiResponse.success(res);
    }


    @Transactional
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        String refreshTokenStr = req.refreshToken;

        if (!jwtTokenProvider.validateToken(refreshTokenStr)
                || !jwtTokenProvider.isRefreshToken(refreshTokenStr)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userIdFromToken = jwtTokenProvider.getUserId(refreshTokenStr);

        RefreshToken stored = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다."));

        if (!stored.getUserId().equals(userIdFromToken) || stored.isExpired()) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었거나 유효하지 않습니다.");
        }

        User user = userRepo.findById(stored.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail()
        );
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(
                user.getId(),
                user.getEmail()
        );

        stored.updateToken(
                newRefreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValiditySeconds())
        );
        refreshTokenRepository.save(stored);

        LoginResponse res = new LoginResponse(
                user.getId(),
                "Bearer",
                newAccessToken,
                newRefreshToken,
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

    @Transactional
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

    @Transactional
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

    @Transactional
    @DeleteMapping
    public ApiResponse<Void> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        refreshTokenRepository.deleteByUserId(user.getId());
        userRepo.delete(user);
        return ApiResponse.success();
    }
}
