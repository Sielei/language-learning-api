package com.hs.languagelearningapi.auth;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.common.exception.UserAuthenticationException;
import com.hs.languagelearningapi.email.EmailUtil;
import com.hs.languagelearningapi.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailUtil emailUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserService userService, AuthenticationManager authenticationManager,
                                 BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, EmailUtil emailUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailUtil = emailUtil;
    }

    public DTO.LoginResponse login(DTO.LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Login attempted by user: {} from IP address: {}", loginRequest.email(), request.getRemoteAddr());
        var userDto = userService.findUserByUsername(loginRequest.email())
                .orElseThrow(() -> new UserAuthenticationException("Failed to authenticate user! Username or password is incorrect"));
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(),
                loginRequest.password());
        var isAuthenticated = false;
        try {
            var authManager = authenticationManager.authenticate(authenticationToken);
            isAuthenticated = authManager.isAuthenticated();
            log.info("{} authenticated successfully", userDto.firstName());
        }
        catch (AuthenticationException authenticationException){
            log.error("Authentication for {} failed because of {}!", userDto.firstName(), authenticationException.getMessage());
        }
        if (isAuthenticated){
            var jwtToken = jwtUtil.generateJWTToken(userDto);
            return new DTO.LoginResponse(jwtToken, "Bearer");
        }
        else {
            throw new UserAuthenticationException("Failed to authenticate user! Username or password is incorrect");
        }
    }
    public void  generatePasswordResetToken(String email) {
        var user = userService.findUserByUsername(email)
                .orElseThrow(() -> new UserAuthenticationException("Email: " +
                        email + " does not exist"));
        var token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(new DTO.CreatePasswordResetTokenCommand(user.id(), token));
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.firstName());
        properties.put("passwordResetToken", token);
        var subject = "Password Reset";
        var template = "password-reset.html";
        emailUtil.sendEmail(email, subject, template, properties);
    }

    public void resetPasswordWithResetToken(DTO.ResetPasswordRequest resetPasswordRequest) {
        var user = userService.findUserByResetToken(resetPasswordRequest.token());
        var newPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.newPassword());
        userService.updatePassword(new DTO.UpdatePasswordCommand(user.id(), newPassword));
    }

    public DTO.RefreshToken getRefreshToken(UUID userId){
        var userDto = userService.findUserById(userId);
        //create a new session
        var jwtToken = jwtUtil.generateJWTToken(userDto);
        //return new token
        return new DTO.RefreshToken(jwtToken, "Bearer");
    }

    public void updatePassword(UUID userId, DTO.UpdatePasswordRequest updatePasswordRequest) {
        var userDto = userService.findUserById(userId);
        if (!bCryptPasswordEncoder.matches(updatePasswordRequest.currentPassword(), userDto.password())){
            throw new UserAuthenticationException("Current password does not match with the password you provided!");
        }
        var newPassword = bCryptPasswordEncoder.encode(updatePasswordRequest.newPassword());
        var updatePasswordCommand =  new DTO.UpdatePasswordCommand(userDto.id(), newPassword);
        userService.updatePassword(updatePasswordCommand);
    }
    public DTO.RegisterUserRequest encodeRawPassword(DTO.RegisterUserRequest registerUserRequest){
        return new DTO.RegisterUserRequest(registerUserRequest.firstName(), registerUserRequest.lastName(),
                registerUserRequest.email(), bCryptPasswordEncoder.encode(registerUserRequest.password()));
    }
}
