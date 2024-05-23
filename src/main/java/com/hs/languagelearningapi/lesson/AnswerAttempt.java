package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "answer_attempts")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private UUID userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    private String userAnswer;
    private Integer marksAwarded;
    private DTO.AttemptStatus status;
}