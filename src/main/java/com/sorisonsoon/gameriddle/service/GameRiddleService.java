package com.sorisonsoon.gameriddle.service;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.domain.entity.GameRiddle;
import com.sorisonsoon.gameriddle.domain.entity.GameRiddleStep;
import com.sorisonsoon.gameriddle.domain.repository.GameRiddleRepository;
import com.sorisonsoon.gameriddle.domain.repository.GameRiddleStepRepository;
import com.sorisonsoon.gameriddle.dto.request.GameFinishRequest;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import com.sorisonsoon.ranking.service.RankingService;
import com.sorisonsoon.record.domain.entity.RecordRiddle;
import com.sorisonsoon.record.domain.repository.RecordRiddleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRiddleService {

    private final RankingService rankingService;
    private final GameRiddleRepository gameRiddleRepository;
    private final GameRiddleStepRepository gameRiddleStepRepository;
    private final RecordRiddleRepository recordRiddleRepository;

    @Transactional(readOnly = true)
    public List<HandQuestionResponse> getGameRiddles(GameDifficulty difficulty, Long totalQuestion) {
        return gameRiddleRepository.getRiddlesByDifficulty(difficulty, totalQuestion);
    }

    @Transactional(readOnly = true)
    public String getQuestionImage(Long riddleId, Long step) {
        GameRiddleStep question = gameRiddleStepRepository.findByRiddleIdAndStep(riddleId, step);
        return question != null ? question.getAnswer() : null;
    }

    @Transactional(readOnly = true)
    public String getQuestionVideo(Long riddleId) {
        GameRiddle question = gameRiddleRepository.findByRiddleId(riddleId);
        return question != null ? question.getVideo() : null;
    }

    public void gameFinish(Long userId, List<GameFinishRequest> finishRequest) {
        List<RecordRiddle> recordRiddles = finishRequest.stream()
                .map(request -> RecordRiddle.of(
                        userId,
                        request.getRiddleId(),
                        GameCategory.RIDDLE,
                        request.getIsCorrect()
                ))
                .toList();

        recordRiddleRepository.saveAll(recordRiddles);

        // 랭킹 저장을 위한 점수 계산
        long totalCount = finishRequest.size();
        long correctCount = finishRequest.stream()
                .filter(GameFinishRequest::getIsCorrect)
                .count();
        double score = totalCount > 0 ? (double) correctCount / totalCount : 0.0;

        rankingService.save(userId, GameCategory.RIDDLE, score);
    }
}


