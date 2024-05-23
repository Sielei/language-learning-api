package com.hs.languagelearningapi.user;

import com.hs.languagelearningapi.common.DTO;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<DTO.UserDto> findUserByUsername(String username);

    DTO.RegisterUserResponse registerUser(DTO.RegisterUserRequest registerUserRequest);

    DTO.UserDto findUserByResetToken(String token);

    void createPasswordResetToken(DTO.CreatePasswordResetTokenCommand createPasswordResetTokenCommand);

    DTO.UserDto findUserById(UUID userId);

    void updatePassword(DTO.UpdatePasswordCommand updatePasswordCommand);
}
