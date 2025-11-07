package com.example.ktbapi.user.api;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.model.UserRole;
import com.example.ktbapi.user.repo.UserJpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserJpaRepository userRepo;

    public UserController(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public ApiResponse<IdResponse> createUser(@RequestBody UserCreateRequest req) {
        User u = new User(req.email, req.password, req.nickname, UserRole.USER);
        userRepo.save(u);
        return ApiResponse.success(new IdResponse(u.getId()));
    }
}

class UserCreateRequest {
    public String email;
    public String password;
    public String nickname;
}
