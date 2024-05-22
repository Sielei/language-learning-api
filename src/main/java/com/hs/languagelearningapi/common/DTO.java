package com.hs.languagelearningapi.common;

import java.util.UUID;

public class DTO {
    public record RegisterUserRequest(String firstName, String lastName, String email, String password){}
    public record RegisterUserResponse(UUID id, String name, String email){}
    public enum Role{
        TUTOR, LEARNER
    }
    public record UserDto(UUID id, String firstName, String lastName, String email,
                          String password, Role role ){}
    public record LoginRequest(String email, String password){}
    public record LoginResponse(String token, String type){}
}
