package com.hs.languagelearningapi.lesson;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "supported_languages")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
}