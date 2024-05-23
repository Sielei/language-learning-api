package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    Optional<Exercise> findByLessonIdAndId(UUID lessonId, UUID exerciseId);

    default Exercise findExerciseById(UUID lessonId, UUID exerciseId){
        return findByLessonIdAndId(lessonId, exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with id: " + exerciseId +
                        " for lesson with id: " + lessonId + " does not exist!"));
    }

    @Query("select e from Exercise e where e.lesson.id = ?1")
    Page<Exercise> findAllLessonExercises(UUID lessonId, Pageable pageable);
}