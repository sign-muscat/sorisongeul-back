package com.sorisonsoon.gameriddle.domain.repository;

import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.domain.entity.GameRiddle;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRiddleRepositoryCustom {

    List<HandQuestionResponse> getRiddlesByDifficulty(GameDifficulty difficulty, Long totalQuestion);
}



