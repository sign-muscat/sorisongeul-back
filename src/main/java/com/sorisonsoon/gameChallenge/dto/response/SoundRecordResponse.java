package com.sorisonsoon.gameChallenge.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SoundRecordResponse {
    private final String inputText;
    private final Double similarity;
}
