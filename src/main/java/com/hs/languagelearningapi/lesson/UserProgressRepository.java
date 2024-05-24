package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface UserProgressRepository extends JpaRepository<UserProgress, UUID> {
    List<UserProgress> findByUserIdAndLessonLanguageIdAndLessonLanguageProficiency(UUID userId, UUID languageId, DTO.LanguageProficiency languageProficiency);

    Optional<UserProgress> findByLessonAndUserId(Lesson lesson, UUID userId);
    default UserProgress findProgressByLesson(Lesson lesson, UUID userId){
        return findByLessonAndUserId(lesson, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No progress record for user"));
    }

    List<UserProgress> findByUserId(UUID userId);
}