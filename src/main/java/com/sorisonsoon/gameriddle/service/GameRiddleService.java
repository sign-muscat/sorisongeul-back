package com.sorisonsoon.gameriddle.service;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.domain.entity.GameRiddle;
import com.sorisonsoon.gameriddle.domain.entity.GameRiddleStep;
import com.sorisonsoon.gameriddle.domain.repository.GameRiddleRepository;
import com.sorisonsoon.gameriddle.domain.repository.GameRiddleStepRepository;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRiddleService {

    private final GameRiddleRepository gameRiddleRepository;
    private final GameRiddleStepRepository gameRiddleStepRepository;

    @Transactional(readOnly = true)
    public List<HandQuestionResponse> getGameRiddles(GameDifficulty difficulty, Long totalQuestion) {
        return gameRiddleRepository.getRiddlesByDifficulty(difficulty, totalQuestion);
    };

    @Transactional(readOnly = true)
    public String getQuestionImage(Long riddleId, Long step) {
        GameRiddleStep question = gameRiddleStepRepository.findByRiddleIdAndStep(riddleId, step);
        return question.getAnswer();
    }

    @Transactional(readOnly = true)
    public String getQuestionVideo(Long riddleId) {
        GameRiddle question = gameRiddleRepository.findByRiddleId(riddleId);
        return question.getVideo();
    }
}
