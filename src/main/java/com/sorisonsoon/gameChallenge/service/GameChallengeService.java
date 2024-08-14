package com.sorisonsoon.gameChallenge.service;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallengeSchedule;
import com.sorisonsoon.gameChallenge.domain.repository.GameChallengeScheduleRepository;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.domain.repository.GameChallengeRepository;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameChallengeService {

    private final GameChallengeRepository gameChallengeRepository;
    private final GameChallengeScheduleRepository gameChallengeScheduleRepository;

    // 매일 자정, 오늘의 문제 출제
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleDailyQuestion() {
        GameChallenge challengeQuestion = gameChallengeRepository.getUnscheduledQuestion();

        gameChallengeScheduleRepository.save(
                new GameChallengeSchedule(
                        challengeQuestion.getChallengeId()
                )
        );
    }

    @Transactional(readOnly = true)
    public SoundCorrectResponse checkCorrect(Long userId) {
        return gameChallengeRepository.checkCorrect(userId);
    }

    @Transactional(readOnly = true)
    public SoundQuestionResponse getSoundQuestion() {
        return gameChallengeRepository.getTodayQuestion().orElseThrow();
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
