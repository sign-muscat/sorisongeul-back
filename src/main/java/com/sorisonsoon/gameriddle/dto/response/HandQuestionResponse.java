package com.sorisonsoon.gameriddle.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "맞수수 문제 응답 DTO")
@Getter
@RequiredArgsConstructor
public class HandQuestionResponse {

    @Schema(description = "문제 고유 ID")
    private final Long riddleId;

    @Schema(description = "문제 단어")
    private final String question;

    @Schema(description = "문제의 동작 수")
    private final Long totalStep;

}
