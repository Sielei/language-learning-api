package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Lessons", description = "API to manage lessons and exercises")
@RestController
@RequestMapping("/api/v1/lessons")
class LessonController {
    private final LessonService lessonService;

    LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary = "Create a Language lesson", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PostMapping
    ResponseEntity<DTO.LessonResponse> createLanguageLesson(@RequestBody DTO.LessonRequest lessonRequest){
        var lesson = lessonService.createLanguageLesson(lessonRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{lessonId}")
                .buildAndExpand(lesson.id())
                .toUri();
        return ResponseEntity.created(location).body(lesson);
    }

    @Operation(summary = "Find Language lesson by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{lessonId}")
    DTO.LessonResponse findLanguageLessonById(@PathVariable("lessonId") UUID lessonId){
        return lessonService.findLanguageLessonById(lessonId);
    }

    @Operation(summary = "Find all language lessons", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping
    DTO.PagedCollection<DTO.LessonResponse> findAllLanguageLessons(
            @RequestParam(name = "languageId") UUID languageId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return lessonService.findAllLanguageLessons(languageId, page, pageSize);
    }

    @Operation(summary = "Update language lesson", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PutMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLanguageLesson(@PathVariable("lessonId") UUID lessonId,
                              @RequestBody DTO.LessonRequest lessonRequest){
        lessonService.updateLanguageLesson(lessonId, lessonRequest);
    }

    @Operation(summary = "Delete language lesson", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLanguageLesson(@PathVariable("lessonId") UUID lessonId){
        lessonService.deleteLanguageLesson(lessonId);
    }

    @Operation(summary = "Create lesson exercise", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PostMapping("/{lessonId}/exercises")
    ResponseEntity<DTO.CreateExerciseResponse> createLessonExercise(
            @PathVariable("lessonId") UUID lessonId,
            @RequestBody DTO.ExerciseRequest exerciseRequest){
        var exercise = lessonService.createLessonExercise(lessonId, exerciseRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{exerciseId}")
                .buildAndExpand(exercise.id())
                .toUri();
        return ResponseEntity.created(location).body(exercise);
    }

    @Operation(summary = "Find lesson exercise by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{lessonId}/exercises/{exerciseId}")
    DTO.ExerciseResponse findLessonExerciseById(@PathVariable("lessonId") UUID lessonId,
                                                      @PathVariable("exerciseId") UUID exerciseId){
        return lessonService.findLessonExerciseById(lessonId, exerciseId);
    }

    @Operation(summary = "Find all lesson exercises", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{lessonId}/exercises")
    DTO.PagedCollection<DTO.ExerciseResponse> findAllLessonExercises(
            @PathVariable("lessonId") UUID lessonId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return lessonService.findAllLessonExercises(lessonId, page, pageSize);
    }

    @Operation(summary = "Update lesson exercise", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PutMapping("/{lessonId}/exercises/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLessonExercise(@PathVariable("lessonId") UUID lessonId,
                              @PathVariable("exerciseId") UUID exerciseId,
                              @RequestBody DTO.ExerciseRequest exerciseRequest){
        lessonService.updateLessonExercise(lessonId, exerciseId, exerciseRequest);

    }

    @Operation(summary = "Delete lesson exercise", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @DeleteMapping("/{lessonId}/exercises/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLessonExercise(@PathVariable("lessonId") UUID lessonId,
                              @PathVariable("exerciseId") UUID exerciseId){
        lessonService.deleteLessonExercise(lessonId, exerciseId);
    }

    @Operation(summary = "Get the next exercise for the lesson", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{lessonId}/exercises/next")
    DTO.PagedCollection<DTO.ExerciseResponse> getNextExercise(@PathVariable("lessonId") UUID lessonId,
                                                              @RequestParam("page") int page){
        return lessonService.findAllLessonExercises(lessonId, page, 1);
    }

    @Operation(summary = "Submit an answer to exercise task", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{lessonId}/exercises/{exerciseId}/response")
    DTO.ResponseFeedback submitAnswer(@PathVariable("lessonId") UUID lessonId, @PathVariable("exerciseId") UUID exerciseId,
                                      @RequestAttribute("userId") UUID userId,
                                      @RequestBody DTO.Answer answer){
        return lessonService.submitExerciseAnswer(lessonId, exerciseId, userId, answer);
    }

    @Operation(summary = "Get lesson recommendation for current user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/recommedations/{languageId}")
    DTO.LessonResponse getLessonRecommendation(@PathVariable("languageId") UUID languageId,
            @RequestAttribute("userId") UUID userId){
        return lessonService.getRecommendation(userId, languageId);
    }

}
