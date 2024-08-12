package com.sorisonsoon.gameChallenge.dto.response;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SoundCorrectResponse {
    private final Boolean isCorrect;
    private final GameDifficulty difficulty;
}
