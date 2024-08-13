package com.sorisonsoon.gameChallenge.domain.repository;

import com.sorisonsoon.gameChallenge.domain.entity.GameChallengeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameChallengeScheduleRepository extends JpaRepository<GameChallengeSchedule, Long> {
}
