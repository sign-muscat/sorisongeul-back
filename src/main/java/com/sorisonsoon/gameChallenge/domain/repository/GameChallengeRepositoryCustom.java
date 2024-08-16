package com.sorisonsoon.gameChallenge.domain.repository;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;

import java.util.List;
import java.util.Optional;

public interface GameChallengeRepositoryCustom {
    Optional<SoundQuestionResponse> getTodayQuestion();

    List<SoundRecordResponse> getSoundRecords(Long userId, Long challengeId);

    GameChallenge getUnscheduledQuestion();

    SoundCorrectResponse checkCorrect(Long userId);
}
