package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LessonService {
    private final LanguageRepository languageRepository;
    private final LessonRepository lessonRepository;

    public LessonService(LanguageRepository languageRepository,
                         LessonRepository lessonRepository) {
        this.languageRepository = languageRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public DTO.LanguageResponse addSuportedLanguage(DTO.LanguageRequest languageRequest) {
        var language = Language.builder().name(languageRequest.name()).build();
        var persistedLanguage = languageRepository.save(language);
        return new DTO.LanguageResponse(persistedLanguage.getId(), persistedLanguage.getName());
    }

    @Transactional(readOnly = true)
    public DTO.PagedCollection<DTO.LanguageResponse> findAllSupportedLanmguages(int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var languagesPage = languageRepository.findAll(pageable)
                .map(language -> new DTO.LanguageResponse(language.getId(), language.getName()));
        return new DTO.PagedCollection<>(
                languagesPage.getContent(),
                languagesPage.getTotalElements(),
                languagesPage.getNumber() + 1,
                languagesPage.getTotalPages(),
                languagesPage.isFirst(),
                languagesPage.isLast(),
                languagesPage.hasNext(),
                languagesPage.hasPrevious()
        );
    }

    @Transactional
    public void updateSupportedLanguage(UUID languageId, DTO.LanguageRequest languageRequest) {
        var languageToUpdate = languageRepository.findLanguageById(languageId);
        languageToUpdate.setName(languageToUpdate.getName());
        languageRepository.save(languageToUpdate);
    }

    @Transactional
    public void deleteSupportedLanguage(UUID languageId) {
        var languageToDelete = languageRepository.findLanguageById(languageId);
        languageRepository.delete(languageToDelete);
    }

    @Transactional(readOnly = true)
    public DTO.LanguageResponse findSupportedLanguageById(UUID languageId) {
        var language = languageRepository.findLanguageById(languageId);
        return new DTO.LanguageResponse(language.getId(), language.getName());
    }

    @Transactional
    public DTO.LessonResponse createLanguageLesson(DTO.LessonRequest lessonRequest) {
        var lesson = mapLessonRequestToLesson(lessonRequest);
        var persistedLesson = lessonRepository.save(lesson);
        return mapLessonToLessonResponse(persistedLesson);
    }

    private DTO.LessonResponse mapLessonToLessonResponse(Lesson lesson) {
        return new DTO.LessonResponse(lesson.getId());
    }

    private Lesson mapLessonRequestToLesson(DTO.LessonRequest lessonRequest) {
        var language = languageRepository.findLanguageById(lessonRequest.languageId());
        return Lesson.builder()
                .language(language)
                .name(lessonRequest.name())
                .description(lessonRequest.description())
                .languageProficiency(lessonRequest.languageProficiency())
                .build();
    }

    @Transactional(readOnly = true)
    public DTO.LessonResponse findLanguageLessonById(UUID lessonId) {
        var lesson = lessonRepository.findLessonById(lessonId);
        return mapLessonToLessonResponse(lesson);
    }

    @Transactional(readOnly = true)
    public DTO.PagedCollection<DTO.LessonResponse> findAllLanguageLessons(String language, int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var lessonsPage = lessonRepository.findAllLanguageLessons(language, pageable)
                .map(this::mapLessonToLessonResponse);
        return new DTO.PagedCollection<>(
                lessonsPage.getContent(),
                lessonsPage.getTotalElements(),
                lessonsPage.getNumber() + 1,
                lessonsPage.getTotalPages(),
                lessonsPage.isFirst(),
                lessonsPage.isLast(),
                lessonsPage.hasNext(),
                lessonsPage.hasPrevious()
        );

    }

    @Transactional
    public void updateLanguageLesson(UUID lessonId, DTO.LessonRequest lessonRequest) {
        var lessonToUpdate = lessonRepository.findLessonById(lessonId);
        var language = languageRepository.findLanguageById(lessonRequest.languageId());
        lessonToUpdate.setLanguage(language);
        lessonToUpdate.setName(lessonToUpdate.getName());
        lessonToUpdate.setDescription(lessonToUpdate.getDescription());
        lessonToUpdate.setLanguageProficiency(lessonRequest.languageProficiency());
        lessonRepository.save(lessonToUpdate);
    }

    @Transactional
    public void deleteLanguageLesson(UUID lessonId) {
        var lessonToDelete = lessonRepository.findLessonById(lessonId);
        lessonRepository.delete(lessonToDelete);
    }
}
