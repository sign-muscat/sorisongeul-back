package com.sorisonsoon.gameChallenge.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
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

    @GetMapping("/game-start")
    public ResponseEntity<SoundQuestionResponse> gameStart(@RequestParam GameDifficulty difficulty) {

        SoundQuestionResponse question = gameChallengeService.getSoundQuestion(difficulty);

        return ResponseEntity.ok(question);
    }

    @GetMapping("/records")
    public ResponseEntity<List<SoundRecordResponse>> getRecords(@RequestParam Long challengeId) {

        List<SoundRecordResponse> records = gameChallengeService.getSoundRecords(challengeId);

        return ResponseEntity.ok(records);
    }

    @PostMapping("/result")
    public ResponseEntity<SoundResultResponse> getResult(@RequestBody SoundResultRequest answerRequest) {

        // TODO: 로그인 한 사용자 정보도 받아서 넘겨야 함

        SoundResultResponse result = gameChallengeService.getResult(answerRequest);

        return ResponseEntity.ok(result);
    }
}
