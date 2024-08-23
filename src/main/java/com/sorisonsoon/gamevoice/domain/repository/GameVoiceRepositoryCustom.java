package com.sorisonsoon.gamevoice.domain.repository;

import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;

public interface GameVoiceRepositoryCustom {

    GameVoiceQuestionResponse getFixedQuestion();
    GameVoiceQuestionResponse createTodayQuestion();
    GameVoiceQuestionResponse geTodayQuestion();
}
