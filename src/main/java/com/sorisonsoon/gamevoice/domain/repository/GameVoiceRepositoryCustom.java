package com.sorisonsoon.gamevoice.domain.repository;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;

public interface GameVoiceRepositoryCustom {

    GameVoiceQuestionResponse getRandomVoiceByDifficulty(GameDifficulty difficulty);
}
