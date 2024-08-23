package com.sorisonsoon.gameriddle.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.dto.request.GameFinishRequest;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import com.sorisonsoon.gameriddle.service.GameRiddleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sign")
public class GameRiddleController {

    private final GameRiddleService gameRiddleService;

    @GetMapping("/game-start")
    public ResponseEntity<List<HandQuestionResponse>> gameStart(@RequestParam GameDifficulty difficulty,
                                                                @RequestParam Long totalQuestion) {
        List<HandQuestionResponse> questionList =
                gameRiddleService.getGameRiddles(difficulty, totalQuestion);

        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/question-image")
    public ResponseEntity<String> getQuestionImage(@RequestParam Long riddleId,
                                                   @RequestParam Long step) {
        String answer = gameRiddleService.getQuestionImage(riddleId, step);

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/question-video")
    public ResponseEntity<String> getQuestionVideo(@RequestParam Long riddleId) {
        String video = gameRiddleService.getQuestionVideo(riddleId);

        return ResponseEntity.ok(video);
    }

    @PostMapping("/game-finish")
    public ResponseEntity<Void> gameFinish(@RequestBody List<GameFinishRequest> finishRequest) {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        gameRiddleService.gameFinish(userId, finishRequest);

        return ResponseEntity.created(URI.create("api/v1/")).build();
    }
}



