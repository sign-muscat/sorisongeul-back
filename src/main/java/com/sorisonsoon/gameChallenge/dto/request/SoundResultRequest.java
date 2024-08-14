package com.sorisonsoon.gameChallenge.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SoundResultRequest {
    private final Long challengeId;
    private final String inputText;
}
