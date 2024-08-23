package com.sorisonsoon.record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "너목보 기록 요청 DTO")
@Getter
public class RecordGameVoiceRequest {

    @Schema(description = "참가자 ID")
    private final Long playerId;

    @Schema(description = "너목보 문제 ID")
    private final Long voiceId;

    @Schema(description = "입력 문장")
    private final String inputText;

    private RecordGameVoiceRequest(
            final Long playerId, final Long voiceId,
            final String inputText
    ) {
        this.playerId = playerId;
        this.voiceId = voiceId;
        this.inputText = inputText;
    }


}
