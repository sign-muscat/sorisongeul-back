package com.sorisonsoon.gamewhisper.domain.entity;

import com.sorisonsoon.gamewhisper.domain.type.GameWhisperCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_whisper")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameWhisper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "whisper_id")
    private Long whisperId;

    private String question;

    @Enumerated(value = EnumType.STRING)
    private GameWhisperCategory category;
}
