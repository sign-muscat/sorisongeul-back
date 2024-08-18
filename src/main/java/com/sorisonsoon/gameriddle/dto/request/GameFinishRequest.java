package com.sorisonsoon.gameriddle.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameFinishRequest {
    private final Long riddleId;
    private final String question;
    private final Long totalStep;
    private final Boolean isCorrect;
}
