package com.hs.languagelearningapi.lesson;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "multiple_choices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MultipleChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    private Integer index;
    @Column(columnDefinition = "boolean default false")
    private Boolean isMatchingChoice;
    private Boolean isLeftChoice;
    private String choice;
}