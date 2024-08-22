package com.sorisonsoon.gamevoice.controller;

import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import com.sorisonsoon.gamevoice.service.GameVoiceService;
import com.sorisonsoon.record.dto.request.RecordGameVoiceRequest;
import com.sorisonsoon.record.dto.response.RecordGameVoiceResponse;
import com.sorisonsoon.user.domain.type.CustomUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voice")
public class GameVoiceController {

    private final GameVoiceService gameVoiceService;
    private final List<RecordGameVoiceResponse> recordGameVoiceResponseList = new ArrayList<>();

    /**
     * 랜덤 문제 생성
     *
     * @param customUser
     */
    @GetMapping("/question")
    public ResponseEntity<ApiResponse<?>> getGameVideoRandomQuestion(@AuthenticationPrincipal CustomUser customUser) {
        GameVoiceQuestionResponse gameVoiceQuestionResponse ;
        if (customUser == null) {
            gameVoiceQuestionResponse = gameVoiceService.findVoiceGameRandomQuestion(null);
        } else {
            Long userId = customUser.getUserId();
            gameVoiceQuestionResponse = gameVoiceService.findVoiceGameRandomQuestion(userId);
        }
        return ResponseEntity.ok(ApiResponse.success("랜덤으로 문제를 가져왔습니다.",gameVoiceQuestionResponse));
    }

    /**
     * 문제 유사도 및 정답 일치 판단
     *
     * @param recordGameVoiceRequest
     */
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<?>> gatCheckAnswerSentenceSimilarity(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody @Valid RecordGameVoiceRequest recordGameVoiceRequest
    ) {
        Long userId = customUser.getUserId();
        System.out.println("컨트롤러의 보이스 아이디 : " + recordGameVoiceRequest);
        final RecordGameVoiceResponse recordGameVoiceResponse = gameVoiceService.saveGameVoice(recordGameVoiceRequest, userId);

        return ResponseEntity.ok(ApiResponse.success("플레이가 기록 되었습니다.", recordGameVoiceResponse));
    }


}
