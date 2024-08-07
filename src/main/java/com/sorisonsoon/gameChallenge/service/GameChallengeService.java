package com.sorisonsoon.gameChallenge.service;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.domain.repository.GameChallengeRepository;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameChallengeService {

    private final GameChallengeRepository gameChallengeRepository;

    @Transactional(readOnly = true)
    public SoundQuestionResponse getSoundQuestion(GameDifficulty difficulty) {
        // TODO: 하루에 한 문제 제한 유무에 따라 로직 변동 가능성 (현재 문제 제한 X)
        return gameChallengeRepository.getQuestionByDifficulty(difficulty).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<SoundRecordResponse> getSoundRecords(Long challengeId) {
        return gameChallengeRepository.getSoundRecords(challengeId);
    }

    public SoundResultResponse getResult(SoundResultRequest answerRequest) {

        // TODO: (1) 전달 받은 문장을 임베딩

        // TODO: (2) 정답 문장의 임베딩 값과 유사도 판단

        // TODO: (3) DB record 저장

        // TODO: (4) SoundResultRequest 에 정답 여부와 유사도 담아서 반환
        return null;
    }
}
