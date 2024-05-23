package com.hs.languagelearningapi.lesson;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_progress")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private UUID userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    private Integer attemptedExercises;
    private Integer pendingExercises;
    private Integer failedExercises;
    private Integer userScore;
    private Integer totalScore;
}