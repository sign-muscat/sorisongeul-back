package com.sorisonsoon.gameChallenge.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SoundResultResponse {
    private final Boolean isCorrect;
    private final Double similarity;
}
