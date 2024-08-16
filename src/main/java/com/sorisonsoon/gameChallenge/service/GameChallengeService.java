package com.sorisonsoon.gameChallenge.service;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.common.exception.NotFoundException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallengeSchedule;
import com.sorisonsoon.gameChallenge.domain.repository.GameChallengeScheduleRepository;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.domain.repository.GameChallengeRepository;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundResultResponse;
import com.sorisonsoon.ranking.service.RankingService;
import com.sorisonsoon.record.domain.entity.Record;
import com.sorisonsoon.record.domain.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static com.sorisonsoon.common.utils.sentenceSimilarity.MatrixUtils.cosineSimilarity;
import static com.sorisonsoon.common.utils.sentenceSimilarity.MatrixUtils.pairwiseCosineSimilarity;
import static com.sorisonsoon.gamevoice.service.GameVoiceService.isCorrectCheck;

@Service
@Transactional
@RequiredArgsConstructor
public class GameChallengeService {

    private final TransformersEmbeddingModel transformersEmbeddingModel;

    private final RankingService rankingService;
    private final GameChallengeRepository gameChallengeRepository;
    private final GameChallengeScheduleRepository gameChallengeScheduleRepository;
    private final RecordRepository recordRepository;

    // 매일 자정, 오늘의 문제 출제
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleDailyQuestion() {
        GameChallenge challengeQuestion = gameChallengeRepository.getUnscheduledQuestion();

        gameChallengeScheduleRepository.save(
                GameChallengeSchedule.of(
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
    public List<SoundRecordResponse> getSoundRecords(Long userId, Long challengeId) {
        return gameChallengeRepository.getSoundRecords(userId, challengeId);
    }

    public SoundResultResponse getResult(Long userId, SoundResultRequest answerRequest) {

        GameChallenge gameChallenge = gameChallengeRepository.findById(answerRequest.getChallengeId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_GAME_QUESTION));

        // 정답과 inputText 임베딩
        List<Double> embeddingAnswer = transformersEmbeddingModel.embed(gameChallenge.getAnswer());
        List<Double> embeddingUserInput = transformersEmbeddingModel.embed(answerRequest.getInputText());

        // 유사도 측정
        Double similarity = cosineSimilarity(embeddingAnswer, embeddingUserInput);

        // 정답 판단 (띄어 쓰기 및 문장 부호 제거)
        Boolean isCorrect = isCorrectCheck(gameChallenge.getAnswer(), answerRequest.getInputText());

        final Record newRecord = Record.of(
                userId,
                answerRequest.getChallengeId(),
                GameCategory.CHALLENGE,
                isCorrect,
                similarity,
                answerRequest.getInputText()
        );

        // DB에 플레이 기록 저장
        recordRepository.save(newRecord);

        // 정답일 경우 랭킹 저장
        if(isCorrect) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            Long count = recordRepository.countByPlayerIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

            rankingService.save(userId, GameCategory.CHALLENGE, (double)1 /count);
        }

        return new SoundResultResponse(isCorrect, similarity);
    }
}
