package com.hs.languagelearningapi.lesson;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exercise_answers")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ExerciseAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multiple_choice_id", nullable = true)
    private MultipleChoice multipleChoice;
    @Column(columnDefinition = "TEXT")
    private String answerText;

}