package com.sorisonsoon.gamevoice.dto.response;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gamevoice.domain.type.GameVoiceCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "너목보 문제 응답 DTO")
public class GameVoiceQuestionResponse {

    @Schema(description = "너목보 문제 ID")
    private final Long voiceId;

    @Schema(description = "정답")
    private final String answer;

    @Schema(description = "문제")
    private final String question;

    @Schema(description = "분류")
    private final GameVoiceCategory category;

    @Schema(description = "난이도")
    private final GameDifficulty difficulty;
}
