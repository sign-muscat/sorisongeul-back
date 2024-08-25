package com.sorisonsoon.gameChallenge.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.request.SoundResultRequest;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundResultResponse;
import com.sorisonsoon.gameChallenge.service.GameChallengeService;
import com.sorisonsoon.user.domain.type.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class GameChallengeController {

    private final GameChallengeService gameChallengeService;

    @GetMapping("/check-correct")
    public ResponseEntity<SoundCorrectResponse> checkCorrect(@AuthenticationPrincipal CustomUser user) {
        SoundCorrectResponse result = null;
        if(user != null) {
            result = gameChallengeService.checkCorrect(user.getUserId());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/game-start")
    public ResponseEntity<SoundQuestionResponse> gameStart() {
        SoundQuestionResponse question = gameChallengeService.getSoundQuestion();

        return ResponseEntity.ok(question);
    }

    @GetMapping("/records")
    public ResponseEntity<List<SoundRecordResponse>> getRecords(@RequestParam Long challengeId,
                                                                @AuthenticationPrincipal CustomUser user
    ) {
        List<SoundRecordResponse> records = gameChallengeService.getSoundRecords(user.getUserId(), challengeId);

        return ResponseEntity.ok(records);
    }

    @PostMapping("/result")
    public ResponseEntity<SoundResultResponse> getResult(@RequestBody SoundResultRequest answerRequest,
                                                         @AuthenticationPrincipal CustomUser user
    ) {
        SoundResultResponse result = gameChallengeService.getResult(user.getUserId(), answerRequest);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-answer")
    public ResponseEntity<Void> resetAnswer(@AuthenticationPrincipal CustomUser user) {
        gameChallengeService.resetUserAnswer(user.getUserId());

        return ResponseEntity.ok().build();
    }
}












