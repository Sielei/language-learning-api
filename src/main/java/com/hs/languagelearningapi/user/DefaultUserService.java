package com.hs.languagelearningapi.user;

import com.hs.languagelearningapi.common.ApplicationConfigData;
import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.common.exception.UserAuthenticationException;
import com.hs.languagelearningapi.common.exception.UserRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
class DefaultUserService implements UserService{
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final ApplicationConfigData applicationConfigData;

    DefaultUserService(UserRepository userRepository, PasswordResetRepository passwordResetRepository, ApplicationConfigData applicationConfigData) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.applicationConfigData = applicationConfigData;
    }

    @Override
    public Optional<DTO.UserDto> findUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(this::mapUserToRegisterUserDto);
    }

    @Transactional
    @Override
    public DTO.RegisterUserResponse registerUser(DTO.RegisterUserRequest registerUserRequest) {
        verifyEmailIsUnique(registerUserRequest.email());
        var newUser = userRepository.save(mapRegisterUserRequestToUser(registerUserRequest));
        return mapUserToRegisterUserResponse(newUser);
    }

    private void verifyEmailIsUnique(String email) {
        var userWithSimilarEmail = userRepository.findByEmail(email);
        if (userWithSimilarEmail.isPresent()){
            throw new UserRegistrationException("User with similar email exists!");
        }
    }

    private User mapRegisterUserRequestToUser(DTO.RegisterUserRequest registerUserRequest) {

        return User.builder()
                .firstName(registerUserRequest.firstName())
                .lastName(registerUserRequest.lastName())
                .email(registerUserRequest.email())
                .password(registerUserRequest.password())
                .role(DTO.Role.LEARNER)
                .build();
    }
    private DTO.RegisterUserResponse mapUserToRegisterUserResponse(User user) {
        return new DTO.RegisterUserResponse(user.getId(), user.getFirstName() + " " + user.getLastName(),
                user.getEmail());
    }

    @Transactional(readOnly = true)
    @Override
    public DTO.UserDto findUserByResetToken(String token) {
        var passwordResetToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new UserAuthenticationException("The reset token: " + token + " is not valid!"));
        if (passwordResetToken.isUsed() || !passwordResetToken.getExpiry().after(new Date(System.currentTimeMillis()))){
            throw new UserAuthenticationException("The token: " + passwordResetToken.getToken() +
                    " is expired or already used!");
        }
        var user = passwordResetToken.getUser();
        return mapUserToRegisterUserDto(user);
    }

    @Transactional
    @Override
    public void createPasswordResetToken(DTO.CreatePasswordResetTokenCommand createPasswordResetTokenCommand) {
        var exp = LocalDate.now().plusDays(applicationConfigData.getPasswordResetTokenExpiryDurationInDays());
        var expiry = Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var user = userRepository.findUserById(createPasswordResetTokenCommand.userId());
        var passwordReset = PasswordReset.builder()
                .token(createPasswordResetTokenCommand.token())
                .user(user)
                .expiry(expiry)
                .isUsed(false)
                .build();
        passwordResetRepository.save(passwordReset);
    }

    @Transactional(readOnly = true)
    @Override
    public DTO.UserDto findUserById(UUID userId) {
        var user = userRepository.findUserById(userId);
        return mapUserToRegisterUserDto(user);
    }

    @Transactional
    @Override
    public void updatePassword(DTO.UpdatePasswordCommand updatePasswordCommand) {
        var user = userRepository.findUserById(updatePasswordCommand.userId());
        user.setPassword(updatePasswordCommand.newPassword());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public DTO.RegisterUserResponse findById(UUID userId) {
        var userDto = findUserById(userId);
        return new DTO.RegisterUserResponse(userDto.id(), userDto.firstName() + " " +
                userDto.lastName(), userDto.email());
    }

    @Transactional
    @Override
    public void updateUserDetails(UUID userId, DTO.UpdateUserRequest updateUserRequest) {
        var userToUpdate = userRepository.findUserById(userId);
        userToUpdate.setFirstName(updateUserRequest.firstName());
        userToUpdate.setLastName(userToUpdate.getLastName());
        userToUpdate.setEmail(userToUpdate.getEmail());
        userRepository.save(userToUpdate);
    }

    private DTO.UserDto mapUserToRegisterUserDto(User user) {
        return new DTO.UserDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getRole());
    }
}
