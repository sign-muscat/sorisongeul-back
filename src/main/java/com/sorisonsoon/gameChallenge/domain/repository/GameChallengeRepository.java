package com.sorisonsoon.gameChallenge.domain.repository;

import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameChallengeRepository extends JpaRepository<GameChallenge, Long> {

}
