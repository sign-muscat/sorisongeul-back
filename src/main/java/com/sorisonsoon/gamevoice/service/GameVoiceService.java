package com.sorisonsoon.gamevoice.service;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gamevoice.domain.repository.GameVoiceRepository;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GameVoiceService {

    private final GameVoiceRepository gameVoiceRepository;
    public GameVoiceQuestionResponse findVoiceGameRandomQuestion(GameDifficulty difficulty) {
        return gameVoiceRepository.getRandomVoiceByDifficulty(difficulty);
    }
}
