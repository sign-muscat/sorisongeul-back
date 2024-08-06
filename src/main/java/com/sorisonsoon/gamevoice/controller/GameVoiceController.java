package com.sorisonsoon.gamevoice.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import com.sorisonsoon.gamevoice.service.GameVoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voice")
public class GameVoiceController {

    private final GameVoiceService gameVoiceService;

    //랜덤 문제 조회
    @GetMapping("/question/{difficulty}")
    public ResponseEntity<ApiResponse> getGameVideoRandomQuestion(@PathVariable final GameDifficulty difficulty) {
        final GameVoiceQuestionResponse gameVoiceQuestionResponse = gameVoiceService.findVoiceGameRandomQuestion(difficulty);
        return ResponseEntity.ok(ApiResponse.success("랜덤으로 문제를 가져왔습니다.",gameVoiceQuestionResponse));
    }


}
