package com.sorisonsoon.gamevoice.controller;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import com.sorisonsoon.gamevoice.service.GameVoiceService;
import com.sorisonsoon.record.dto.request.RecordGameVoiceRequest;
import com.sorisonsoon.record.dto.response.RecordGameVoiceResponse;
import com.sorisonsoon.record.dto.response.RecordGameVoiceResponseList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voice")
public class GameVoiceController {

    private final GameVoiceService gameVoiceService;
    private final List<RecordGameVoiceResponse> recordGameVoiceResponseList = new ArrayList<>();

    @GetMapping("/question/{difficulty}")
    public ResponseEntity<ApiResponse<?>> getGameVideoRandomQuestion(@PathVariable final GameDifficulty difficulty) {
        final GameVoiceQuestionResponse gameVoiceQuestionResponse = gameVoiceService.findVoiceGameRandomQuestion(difficulty);
        return ResponseEntity.ok(ApiResponse.success("랜덤으로 문제를 가져왔습니다.",gameVoiceQuestionResponse));
    }

    //문제 유사도 판단 및 정답 일치 판단
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<?>> gatCheckAnswerSentenceSimilarity(@RequestBody @Valid RecordGameVoiceRequest recordGameVoiceRequest) {
        System.out.println("컨트롤러의 보이스 아이디 : " + recordGameVoiceRequest);
        final RecordGameVoiceResponse recordGameVoiceResponse = gameVoiceService.saveGameVoice(recordGameVoiceRequest);

        return ResponseEntity.ok(ApiResponse.success("플레이가 기록 되었습니다.", recordGameVoiceResponse));
    }

    @PostMapping("/checkList")
    public ResponseEntity<ApiResponse<?>> gatCheckAnswerSentenceSimilarityList(@RequestBody @Valid RecordGameVoiceRequest recordGameVoiceRequest) {
        System.out.println("컨트롤러의 보이스 아이디 : " + recordGameVoiceRequest);
        final RecordGameVoiceResponse recordGameVoiceResponse = gameVoiceService.saveGameVoice(recordGameVoiceRequest);

        recordGameVoiceResponseList.add(recordGameVoiceResponse);

        RecordGameVoiceResponseList responseList = new RecordGameVoiceResponseList(recordGameVoiceResponseList);

        return ResponseEntity.ok(ApiResponse.success("플레이가 기록 되었습니다.", responseList));
    }

}
