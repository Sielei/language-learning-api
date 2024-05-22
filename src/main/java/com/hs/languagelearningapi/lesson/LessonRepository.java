package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

interface LessonRepository extends JpaRepository<Lesson, UUID> {
    default Lesson findLessonById(UUID lessonId){
        return findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson with id: " + lessonId +
                        " does not exist!"));
    }

    @Query("select l from Lesson l where  l.language.name = ?1")
    Page<Lesson> findAllLanguageLessons(String language, Pageable pageable);
}