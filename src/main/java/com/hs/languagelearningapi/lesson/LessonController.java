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
}
