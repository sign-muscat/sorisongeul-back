package com.sorisonsoon.gameriddle.domain.repository;

import com.sorisonsoon.gameriddle.domain.entity.GameRiddleStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRiddleStepRepository extends JpaRepository<GameRiddleStep, Long> {
    GameRiddleStep findByRiddleIdAndStep(Long riddleId, Long step);
}
