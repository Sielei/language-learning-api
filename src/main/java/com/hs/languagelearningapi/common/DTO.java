package com.hs.languagelearningapi.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.UUID;

public class DTO {
    public record RegisterUserRequest(@NotBlank(message = "First name is required") String firstName,
                                      @NotBlank(message = "Last name is required") String lastName,
                                      @Email(message = "Email should be valid") String email,
                                      @NotBlank(message = "Password is required") String password){}
    public record RegisterUserResponse(UUID id, String name, String email, Role role){}
    public enum Role{
        TUTOR, LEARNER
    }
    public enum LanguageProficiency{
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    public record UserDto(UUID id, String firstName, String lastName, String email,
                          String password, Role role ){}
    public record LoginRequest(String email, String password){}
    public record LoginResponse(String token, String type){}
    public record ResetPasswordRequest(@NotBlank(message = "Reset token is required")
                                       @Pattern(message = "Invalid password reset token",
                                               regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
                                       String token,
                                       @NotBlank(message = "New password is required")
                                       String newPassword) {}
    public record UpdatePasswordCommand(UUID userId, String newPassword) {}
    public record CreatePasswordResetTokenCommand(UUID userId, String token) {}
    public record GetResetTokenResponse(String success, String message){}
    public record UpdateUserRequest(@NotBlank(message = "First name is required") String firstName,
                                    @NotBlank(message = "Last name is required") String lastName,
                                    @Email(message = "Email should be valid") String email){}
    public record UpdatePasswordRequest(@NotBlank(message = "Current password is required") String currentPassword,
                                        @NotBlank(message = "New password is required")String newPassword) {}
    public record RefreshToken(String token, String type) {}
    public record GetResetTokenRequest(@Email(message = "Email should be valid") String email){}
    public record LanguageRequest(@NotBlank(message = "Language name is required") String name){}
    public record LanguageResponse(UUID id, String name){}
    public record PagedCollection<T>(List<T> data, long totalElements, Integer pageNumber, Integer totalPages,
                                     @JsonProperty("isFirst") boolean isFirst, @JsonProperty("isLast") boolean isLast,
                                     @JsonProperty("hasNext") boolean hasNext, @JsonProperty("hasPrevious") boolean hasPrevious){}
    public record LessonRequest(UUID languageId, LanguageProficiency languageProficiency, Integer index,
                                Integer percentagePassMark, String name, String description){}
    public record LessonResponse(UUID id, Integer index, String name, String description, String language,
                                 LanguageProficiency languageProficiency){}
    public record MultipleChoiceDto(Integer index, String choice){}
    public record MatchingChoice(Integer index, String choice){}
    public record Answer(List<Integer> multipleChoices, List<List<String>> matchingPairs, String answerText){}
    public record ExerciseRequest(Integer index, String task, Integer taskScore, TaskType taskType,
                                  List<MultipleChoiceDto> multipleChoices, List<MatchingChoice> matchingChoices,
                                  Answer answer){}
    public record CreateExerciseResponse(UUID id, Integer index, String task, Integer taskScore, TaskType taskType,
                                         List<MultipleChoiceDto> multipleChoices){}
    public record ExerciseResponse(UUID id, Integer index, String task, Integer taskScore, TaskType taskType,
                                         List<MultipleChoiceDto> multipleChoices){}
    public enum TaskType {
        MULTIPLE_CHOICE_SINGLE_ANSWER, MULTIPLE_CHOICE_MULTIPLE_ANSWERS, SHORT_ANSWER, MATCHING
    }

    public enum AttemptStatus {
        PASSED, FAILED, PARTIAL_FAIL
    }
    public record ResponseFeedback(String status, Integer score, Integer completedExercises,
                                   Integer pendingExercises, Integer totalScore){}

    public record UserProgressDto(String language, String lesson, Integer attemptedExercises,
                               Integer failedExercises, Integer userScore, Integer totalScore){}

}
