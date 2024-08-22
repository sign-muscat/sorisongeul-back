package com.sorisonsoon.gameChallenge.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundResultResponse;
import com.sorisonsoon.gameChallenge.service.GameChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class GameChallengeController {

    private final GameChallengeService gameChallengeService;

    @GetMapping("/check-correct")
    public ResponseEntity<SoundCorrectResponse> checkCorrect() {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        SoundCorrectResponse result = gameChallengeService.checkCorrect(userId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/game-start")
    public ResponseEntity<SoundQuestionResponse> gameStart() {

        SoundQuestionResponse question = gameChallengeService.getSoundQuestion();

        return ResponseEntity.ok(question);
    }

    @GetMapping("/records")
    public ResponseEntity<List<SoundRecordResponse>> getRecords(@RequestParam Long challengeId) {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        List<SoundRecordResponse> records = gameChallengeService.getSoundRecords(userId, challengeId);

        return ResponseEntity.ok(records);
    }

    @PostMapping("/result")
    public ResponseEntity<SoundResultResponse> getResult(@RequestBody SoundResultRequest answerRequest) {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        SoundResultResponse result = gameChallengeService.getResult(userId, answerRequest);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-answer")
    public ResponseEntity<Void> resetAnswer() {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        gameChallengeService.resetUserAnswer(userId);

        return ResponseEntity.ok().build();
    }
}












