package com.sorisonsoon.gameChallenge.domain.entity;

import com.sorisonsoon.gameChallenge.domain.type.GameChallengeCategory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_challenge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long challengeId;

    private String question;

    @Enumerated(value = EnumType.STRING)
    private GameChallengeCategory category;

    @Enumerated(value = EnumType.STRING)
    private GameDifficulty difficulty;
}
