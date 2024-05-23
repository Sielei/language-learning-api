package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Create a Language lesson")
    @PostMapping
    ResponseEntity<DTO.LessonResponse> createLanguageLesson(@RequestBody DTO.LessonRequest lessonRequest){
        var lesson = lessonService.createLanguageLesson(lessonRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{lessonId}")
                .buildAndExpand(lesson.id())
                .toUri();
        return ResponseEntity.created(location).body(lesson);
    }

    @Operation(summary = "Find Language lesson by Id")
    @GetMapping("/{lessonId}")
    DTO.LessonResponse findLanguageLessonById(@PathVariable("lessonId") UUID lessonId){
        return lessonService.findLanguageLessonById(lessonId);
    }

    @Operation(summary = "Find all language lessons")
    @GetMapping
    DTO.PagedCollection<DTO.LessonResponse> findAllLanguageLessons(
            @RequestParam(name = "language", defaultValue = "All") String language,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return lessonService.findAllLanguageLessons(language, page, pageSize);
    }

    @Operation(summary = "Update language lesson")
    @PutMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLanguageLesson(@PathVariable("lessonId") UUID lessonId,
                              @RequestBody DTO.LessonRequest lessonRequest){
        lessonService.updateLanguageLesson(lessonId, lessonRequest);
    }

    @Operation(summary = "Delete language lesson")
    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLanguageLesson(@PathVariable("lessonId") UUID lessonId){
        lessonService.deleteLanguageLesson(lessonId);
    }

    @Operation(summary = "Create lesson exercise")
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

    @Operation(summary = "Find lesson exercise by Id")
    @GetMapping("/{lessonId}/exercises/{exerciseId}")
    DTO.ExerciseResponse findLessonExerciseById(@PathVariable("lessonId") UUID lessonId,
                                                      @PathVariable("exerciseId") UUID exerciseId){
        return lessonService.findLessonExerciseById(lessonId, exerciseId);
    }

    @Operation(summary = "Find all lesson exercises")
    @GetMapping("/{lessonId}/exercises")
    DTO.PagedCollection<DTO.ExerciseResponse> findAllLessonExercises(
            @PathVariable("lessonId") UUID lessonId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return lessonService.findAllLessonExercises(lessonId, page, pageSize);
    }

    @Operation(summary = "Update lesson exercise")
    @PutMapping("/{lessonId}/exercises/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLessonExercise(@PathVariable("lessonId") UUID lessonId,
                              @PathVariable("exerciseId") UUID exerciseId,
                              @RequestBody DTO.ExerciseRequest exerciseRequest){
        lessonService.updateLessonExercise(lessonId, exerciseId, exerciseRequest);

    }

    @Operation(summary = "Delete lesson exercise")
    @DeleteMapping("/{lessonId}/exercises/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLessonExercise(@PathVariable("lessonId") UUID lessonId,
                              @PathVariable("exerciseId") UUID exerciseId){
        lessonService.deleteLessonExercise(lessonId, exerciseId);
    }
}
