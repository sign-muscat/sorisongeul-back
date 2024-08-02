package com.sorisonsoon.gameriddle.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "game_riddle_step")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameRiddleStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riddleStepId;

    private Long riddleId;

    private Long step;

    private String answer;
}
