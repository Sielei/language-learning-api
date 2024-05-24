package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AnswerAttemptRepository extends JpaRepository<AnswerAttempt, UUID> {

    @Query("select count (a) from AnswerAttempt a where a.userId =?1 and a.exercise.lesson.id = ?2 and a.status=?3")
    Integer findFailedExercises(UUID userId, UUID lessonId, DTO.AttemptStatus status);

    @Query("select sum(a.marksAwarded) from AnswerAttempt a where a.userId = ?1 and a.exercise.lesson.id = ?2")
    Integer findCurrentUserScore(UUID userId, UUID lessonId);

    Optional<AnswerAttempt> findByUserIdAndExerciseId(UUID userId, UUID exerciseId);
}