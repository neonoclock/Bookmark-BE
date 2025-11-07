package com.example.ktbapi.user.dto;

public class UserProjectionDto {
    private final Long id;
    private final String email;
    private final String nickname;

    public UserProjectionDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }

    @Override
    public String toString() {
        return "UserProjectionDto{id=%d, email=%s, nickname=%s}".formatted(id, email, nickname);
    }
}
