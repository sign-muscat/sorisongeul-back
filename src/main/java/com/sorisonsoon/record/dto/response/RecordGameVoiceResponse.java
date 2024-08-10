package com.sorisonsoon.record.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.record.domain.entity.Record;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "너목보 기록 응답 DTO")
@Getter
@RequiredArgsConstructor
public class RecordGameVoiceResponse {

    @Schema(description = "생성된 기록 ID")
    private final Long recordId;

    @Schema(description = "참가자 ID")
    private final Long playerId;

    @Schema(description = "문제 ID")
    private final Long voiceId;

    @Schema(description = "분류")
    private final GameCategory category;

    @Schema(description = "정답 유무")
    private final Boolean isCorrect;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @Schema(description = "유사도")
    private final Double similarity;

    @Schema(description = "입력 문장")
    private final String inputText;

    public static RecordGameVoiceResponse form(final Record record) {
        return new RecordGameVoiceResponse (
                record.getRecordId(),
                record.getPlayerId(),
                record.getVoiceId(),
                record.getCategory(),
                record.getIsCorrect(),
                record.getCreatedAt(),
                record.getSimilarity(),
                record.getInputText()
        );
    }


}
