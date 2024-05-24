package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

interface LessonRepository extends JpaRepository<Lesson, UUID> {
    default Lesson findLessonById(UUID lessonId){
        return findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson with id: " + lessonId +
                        " does not exist!"));
    }

    @Query("select l from Lesson l where  l.language.id = ?1")
    Page<Lesson> findAllLanguageLessons(UUID languageId, Pageable pageable);

    @Query("select l from Lesson l where l.language.id = ?1 and l.languageProficiency = ?2 and l.index = (select min(ls.index) from Lesson ls)")
    Optional<Lesson> findFirstLesson(UUID languageId, DTO.LanguageProficiency proficiency);

    default Lesson getFirstLesson(UUID languageId, DTO.LanguageProficiency proficiency){
        return findFirstLesson(languageId, proficiency)
                .orElseThrow(() -> new ResourceNotFoundException("No lesson exists for this language and" +
                        " proficiency level"));
    }
    @Query("select l from Lesson l where l.language.id = ?1 and l.languageProficiency = ?2 and l.index > ?3")
    Optional<Lesson> findNextLesson(UUID languageId, DTO.LanguageProficiency languageProficiency, Integer index);

}