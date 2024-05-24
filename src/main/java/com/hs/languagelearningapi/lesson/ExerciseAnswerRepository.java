package com.hs.languagelearningapi.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, UUID> {
    List<ExerciseAnswer> findAllByExercise(Exercise exercise);
}