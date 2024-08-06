package com.sorisonsoon.gameChallenge.domain.repository;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;

import java.util.List;
import java.util.Optional;

public interface GameChallengeRepositoryCustom {
    Optional<SoundQuestionResponse> getQuestionByDifficulty(GameDifficulty difficulty);

    List<SoundRecordResponse> getSoundRecords(Long challengeId);
}
