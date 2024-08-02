package com.sorisonsoon.gameriddle.domain.entity;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.domain.type.GameRiddleCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "game_riddle")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameRiddle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "riddle_id")
    private Long riddleId;

    private String question;
    private String video;

    @Enumerated(EnumType.STRING)
    private GameRiddleCategory category;

    @Enumerated(EnumType.STRING)
    private GameDifficulty difficulty;
}
