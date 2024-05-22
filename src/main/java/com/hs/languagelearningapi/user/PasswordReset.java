package com.hs.languagelearningapi.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "password_resets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Date expiry;
    private boolean used;
}