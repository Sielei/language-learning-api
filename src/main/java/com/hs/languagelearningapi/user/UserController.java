package com.hs.languagelearningapi.user;

import com.hs.languagelearningapi.common.DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Users", description = "API to manage users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Find user by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{userId}")
    DTO.RegisterUserResponse findUserById(@PathVariable("userId") UUID userId){
        return userService.findById(userId);
    }

    @Operation(summary = "Find All Registered users", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping
    DTO.PagedCollection<DTO.RegisterUserResponse> findAllUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return userService.findAllUsers(page, pageSize);
    }

    @Operation(summary = "Get  learner's learning progress", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{userId}/progress")
    DTO.PagedCollection<DTO.UserProgressDto> findUserProgress (
            @PathVariable("userId") UUID userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return userService.findUserProgress(userId, page, pageSize);
    }

    @Operation(summary = "Get my own learning progress", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/progress")
    DTO.PagedCollection<DTO.UserProgressDto> findOwnProgress (
            @RequestAttribute("userId") UUID userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return userService.findOwnProgress(userId, page, pageSize);
    }



    @Operation(summary = "Update user details", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@RequestAttribute("userId") UUID userId, @Valid @RequestBody DTO.UpdateUserRequest updateUserRequest){
        userService.updateUserDetails(userId, updateUserRequest);
    }

}
