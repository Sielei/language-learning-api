package com.hs.languagelearningapi.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, UUID> {
}