package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface MultipleChoiceRepository extends JpaRepository<MultipleChoice, UUID> {
    default MultipleChoice findMultipleChoiceById(UUID multipleChoiceId){
        return findById(multipleChoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Multiple choice with id: " +
                        multipleChoiceId + " does not exist!"));
    }

    Optional<MultipleChoice> findByExerciseAndIndex(Exercise exercise, Integer index);

    default MultipleChoice findMultipleChoiceByExerciseAndIndex(Exercise exercise, Integer index){
        return findByExerciseAndIndex(exercise, index)
                .orElseThrow(() -> new ResourceNotFoundException("Multiple choice cannot be found"));
    }
}