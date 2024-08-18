package com.sorisonsoon.gameChallenge.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Table(name = "game_challenge_schedule")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameChallengeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeScheduleId;

    private Long challengeId;

    private LocalDate schedule;

    private GameChallengeSchedule(Long challengeId) {
        this.challengeId = challengeId;
    }

    public static GameChallengeSchedule of(Long challengeId) {
        return new GameChallengeSchedule(challengeId);
    }
}
