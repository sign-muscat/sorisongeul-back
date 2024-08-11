package com.sorisonsoon.gamevoice.domain.entity;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gamevoice.domain.type.GameVoiceCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "game_voice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voiceId")
    private Long voiceId;

    private String answer;
    private String question;

    @Enumerated(EnumType.STRING)
    private GameVoiceCategory category;

    @Enumerated(EnumType.STRING)
    private GameDifficulty difficulty;
}
