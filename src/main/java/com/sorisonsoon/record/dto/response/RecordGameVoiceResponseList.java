package com.sorisonsoon.record.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Schema(description = "너목보 기록 응답 DTO LIST")
@Getter
@RequiredArgsConstructor
public class RecordGameVoiceResponseList {

    @Schema(description = "너목보 기록 응답 리스트")
    private final List<RecordGameVoiceResponse> recordGameVoiceResponseList;

}
