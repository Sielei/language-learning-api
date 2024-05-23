package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lessons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    private Integer index;
    private String description;
    private Integer percentagePassMark;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
    @Enumerated(EnumType.STRING)
    private DTO.LanguageProficiency languageProficiency;
}