package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exercises")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    private Integer index;
    @Column(columnDefinition = "TEXT")
    private String task;
    private Integer taskScore;
    @Enumerated(EnumType.STRING)
    private DTO.TaskType taskType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "exercise")
    private List<MultipleChoice> multipleChoices = new ArrayList<>();
}