package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LessonService {
    private final LanguageRepository languageRepository;

    public LessonService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
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

    public DTO.LanguageResponse findSupportedLanguageById(UUID languageId) {
        var language = languageRepository.findLanguageById(languageId);
        return new DTO.LanguageResponse(language.getId(), language.getName());
    }
}
