package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface LanguageRepository extends JpaRepository<Language, UUID> {
    default Language findLanguageById(UUID languageId){
        return findById(languageId)
                .orElseThrow(() -> new ResourceNotFoundException("Language with id: " +
                        languageId + " does not exists!"));
    }
}