package com.sorisonsoon.gameChallenge.dto.response;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SoundQuestionResponse {
    private final Long challengeId;
    private final String url;
    private final GameDifficulty difficulty;
}
