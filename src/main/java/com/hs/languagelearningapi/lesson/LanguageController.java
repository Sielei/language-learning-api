package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Languages", description = "API to manage languages that learners can learn")
@RestController
@RequestMapping("/api/v1/languages")
class LanguageController {
    private final LessonService lessonService;

    LanguageController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary = "Add supported language", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PostMapping
    ResponseEntity<DTO.LanguageResponse> addSupportedLanguage(@RequestBody @Valid DTO.LanguageRequest languageRequest){
        var supportedLanguage = lessonService.addSupportedLanguage(languageRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{languageId}")
                .buildAndExpand(supportedLanguage.id())
                .toUri();
        return ResponseEntity.created(location).body(supportedLanguage);
    }

    @Operation(summary = "Find supported language by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @GetMapping("/{languageId}")
    DTO.LanguageResponse findSupportedLanguageById(@PathVariable("languageId") UUID languageId){
        return lessonService.findSupportedLanguageById(languageId);
    }

    @Operation(summary = "Find all supported languages", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    DTO.PagedCollection<DTO.LanguageResponse> findAllSupportedLanguages(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return lessonService.findAllSupportedLanmguages(page, pageSize);
    }

    @Operation(summary = "Update supported language", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @PutMapping("/{languageId}")
    DTO.LanguageResponse updateSupportedLanguage(@PathVariable("languageId") UUID languageId,
                                                 @RequestBody DTO.LanguageRequest languageRequest){
        return lessonService.updateSupportedLanguage(languageId, languageRequest);
    }

    @Operation(summary = "Delete supported language", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('TUTOR')")
    @DeleteMapping("/{languageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSupportedLanguage(@PathVariable("languageId") UUID languageId){
        lessonService.deleteSupportedLanguage(languageId);
    }

    @Operation(summary = "Start learning a new language", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{languageId}/start")
    DTO.LessonResponse startLearning(@PathVariable("languageId") UUID languageId,
                                     @RequestParam("proficiency")DTO.LanguageProficiency proficiency,
                                     @RequestAttribute("userId") UUID userId){
        return lessonService.findLessonByLanguageProficiencyAndPriority(userId,languageId, proficiency);
    }

}
