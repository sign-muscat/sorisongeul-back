package com.sorisonsoon.gamevoice.service;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.common.exception.ConflictException;
import com.sorisonsoon.common.exception.NotFoundException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.common.utils.sentenceSimilarity.MatrixUtils;
import com.sorisonsoon.gamevoice.domain.entity.GameVoice;
import com.sorisonsoon.gamevoice.domain.repository.GameVoiceRepository;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import com.sorisonsoon.record.domain.entity.Record;
import com.sorisonsoon.record.domain.repository.RecordRepository;
import com.sorisonsoon.record.dto.request.RecordGameVoiceRequest;
import com.sorisonsoon.record.dto.response.RecordGameVoiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sorisonsoon.common.domain.type.GameCategory.VOICE;

@Service
@Transactional
@RequiredArgsConstructor
public class GameVoiceService {

    private final TransformersEmbeddingModel transformersEmbeddingModel;
    private final GameVoiceRepository gameVoiceRepository;
    private final RecordRepository recordRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void createTodayQuestion() {
        gameVoiceRepository.createTodayQuestion();
    }

    @Transactional
    public GameVoiceQuestionResponse findVoiceGameRandomQuestion(Long userId) {

        if (userId == null) {
            return gameVoiceRepository.getFixedQuestion();
        }

        // 1. 오늘의 문제를 조회함. (문제 번호 반환)
        GameVoiceQuestionResponse todayQuestion = gameVoiceRepository.geTodayQuestion();
        Long voiceId = todayQuestion.getVoiceId();

        // 2. 문제 번호로, 게임 기록을 조회
        Optional<Record> userRecord = recordRepository.findByVoiceIdAndPlayerIdAndIsCorrectTrue(voiceId, userId);

        // 3. 게임 기록이 있는지 판단
        if (userRecord.isPresent()) {
            // 유저가 이미 문제를 풀었다면 null을 반환
            throw new ConflictException(ExceptionCode.ALREADY_QUESTION_CORRECT);
        } else {
            // 유저의 기록이 없다면 문제를 반환
            return todayQuestion;
        }
    }

    @Transactional
    public RecordGameVoiceResponse saveGameVoice(RecordGameVoiceRequest recordGameVoiceRequest) {
        System.out.println("보이스아이디 : "+recordGameVoiceRequest.getVoiceId());
        GameCategory category = VOICE;
        Boolean isCorrect = null;
        Double similarity = 0.0;

        // 문제 조회
        GameVoice gameVoice = gameVoiceRepository.findById(recordGameVoiceRequest.getVoiceId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.INVALID_GAME_VOICE_ID));

        // 원본 문자열
        String origin_game_answer = gameVoice.getAnswer();
        String origin_input_text = recordGameVoiceRequest.getInputText();

        // 정답 체크 및 오답시 유사도 제공
        if(isCorrectCheck(origin_game_answer, origin_input_text)) {
            isCorrect = true;
            System.out.println("정답!");
        } else {
            isCorrect = false;
            System.out.println("땡땡!");
            List<String> texts = List.of(cleanString(origin_game_answer), cleanString(origin_input_text));
            try {
                similarity = getSimilarity(texts);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } ;

        // DB 저장
        final Record newRecord = Record.of(
                recordGameVoiceRequest.getPlayerId(),
                recordGameVoiceRequest.getVoiceId(),
                category,
                isCorrect,
                similarity,
                recordGameVoiceRequest.getInputText()
        );

        final Record record = recordRepository.save(newRecord);

        return RecordGameVoiceResponse.form(record);
    }

    // 메서드
    public static Boolean isCorrectCheck (String origin_game_answer, String origin_input_text) {
        //공백 제거 및 대 소문자 무시 하는 문자열
        String game_answer = cleanString(origin_game_answer);
        String input_text = cleanString(origin_input_text);

        return game_answer.equals(input_text);
    }

    private static String cleanString(String str) {
        // 문자열에서 공백과 특수문자 제거, 소문자로 변환
        return str.replaceAll("[\\p{Punct}\\s]", "").toLowerCase();
    }

    private double getSimilarity(List<String> texts) throws Exception {
        if (texts.size() != 2) {
            throw new IllegalArgumentException("2개의 텍스트를 배열로 입력해 주세요");
        }
        double[][] embeddings = getEmbeddings(texts);
        return MatrixUtils.pairwiseCosineSimilarity(embeddings, embeddings)[0][1];
    }

    private double[][] getEmbeddings(List<String> texts) {
        return transformersEmbeddingModel.embed(texts).stream()
                .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new);
    }

}
